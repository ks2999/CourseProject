// API для работы с уроками
const lessonApi = {
    async getAll() {
        return api.get('/lessons');
    },

    async getPublished() {
        return api.get('/lessons/published');
    },

    async getById(id) {
        return api.get(`/lessons/${id}`);
    },

    async getByTopic(topic) {
        return api.get(`/lessons/topic/${topic}`);
    }
};

// Загрузка уроков при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'index.html';
        return;
    }

    await loadLessons();
});

// Загрузка списка уроков
async function loadLessons() {
    try {
        const lessons = await lessonApi.getPublished();
        displayLessons(lessons);
    } catch (error) {
        showMessage('Ошибка при загрузке уроков: ' + error.message, 'error');
        document.getElementById('lessons-list').innerHTML = '<p class="loading">Ошибка загрузки уроков</p>';
    }
}

// Отображение списка уроков
function displayLessons(lessons) {
    const lessonsList = document.getElementById('lessons-list');
    
    if (lessons.length === 0) {
        lessonsList.innerHTML = '<p class="loading">Нет доступных уроков</p>';
        return;
    }

    // Сортируем уроки по порядку
    lessons.sort((a, b) => (a.orderNumber || 0) - (b.orderNumber || 0));
    
    lessonsList.innerHTML = lessons.map(lesson => `
        <div class="lesson-card" onclick="openLesson('${lesson.id}')">
            <div class="lesson-header">
                <h3>📖 ${escapeHtml(lesson.title)}</h3>
                ${lesson.topic ? `<span class="lesson-topic">${escapeHtml(lesson.topic)}</span>` : ''}
            </div>
            <div class="lesson-description">${escapeHtml(lesson.description || 'Описание отсутствует')}</div>
            <div class="lesson-footer">
                <span class="lesson-order">📚 Урок ${lesson.orderNumber || '?'}</span>
                <a href="tasks.html?lesson=${lesson.id}" class="btn btn-secondary btn-small" onclick="event.stopPropagation();">Задачи урока</a>
            </div>
        </div>
    `).join('');
}

// Фильтрация уроков
async function filterLessons() {
    const topic = document.getElementById('topic-filter').value.trim();
    
    if (!topic) {
        await loadLessons();
        return;
    }

    try {
        const lessons = await lessonApi.getByTopic(topic);
        displayLessons(lessons);
    } catch (error) {
        showMessage('Ошибка при поиске: ' + error.message, 'error');
    }
}

// Открытие урока
async function openLesson(lessonId) {
    try {
        const lesson = await lessonApi.getById(lessonId);
        
        // Парсим markdown содержимое
        const contentHtml = parseMarkdown(lesson.content || lesson.description || 'Содержимое урока отсутствует');
        
        // Создаем модальное окно с содержимым урока
        const modal = document.createElement('div');
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content lesson-modal">
                <span class="modal-close" onclick="this.closest('.modal').remove()">&times;</span>
                <div class="lesson-header-section">
                    <h2>${lesson.title}</h2>
                    ${lesson.topic ? `<div class="lesson-topic-badge">${lesson.topic}</div>` : ''}
                </div>
                <div class="lesson-content-formatted">${contentHtml}</div>
                <div class="modal-footer">
                    <button onclick="this.closest('.modal').remove()" class="btn btn-primary">Закрыть</button>
                    <a href="tasks.html?lesson=${lesson.id}" class="btn btn-secondary">Перейти к задачам</a>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
        
        // Подсветка синтаксиса для блоков кода
        highlightCodeBlocks(modal);
    } catch (error) {
        showMessage('Ошибка при загрузке урока: ' + error.message, 'error');
    }
}

// Экранирование HTML для безопасности
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Подсветка синтаксиса кода (простая версия)
function highlightCodeBlocks(container) {
    const codeBlocks = container.querySelectorAll('pre.code-block code');
    codeBlocks.forEach(block => {
        let code = block.textContent;
        
        // Простая подсветка для C
        code = code
            // Ключевые слова
            .replace(/\b(int|char|float|double|void|if|else|for|while|do|return|break|continue|switch|case|default|struct|typedef|enum|static|const|extern|volatile|register|signed|unsigned|long|short|sizeof|include|define|ifdef|ifndef|endif)\b/g, 
                '<span class="keyword">$1</span>')
            // Строки
            .replace(/"([^"]*)"/g, '<span class="string">"$1"</span>')
            .replace(/'([^']*)'/g, '<span class="string">\'$1\'</span>')
            // Комментарии
            .replace(/\/\/.*$/gm, '<span class="comment">$&</span>')
            .replace(/\/\*[\s\S]*?\*\//g, '<span class="comment">$&</span>')
            // Числа
            .replace(/\b(\d+\.?\d*)\b/g, '<span class="number">$1</span>')
            // Функции
            .replace(/\b([a-zA-Z_][a-zA-Z0-9_]*)\s*(?=\()/g, '<span class="function">$1</span>');
        
        block.innerHTML = code;
    });
}

