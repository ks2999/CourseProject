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

    lessonsList.innerHTML = lessons.map(lesson => `
        <div class="lesson-card" onclick="openLesson('${lesson.id}')">
            <div class="lesson-header">
                <h3>${lesson.title}</h3>
                ${lesson.topic ? `<span class="lesson-topic">${lesson.topic}</span>` : ''}
            </div>
            <div class="lesson-description">${lesson.description || 'Описание отсутствует'}</div>
            <div class="lesson-footer">
                <span class="lesson-order">Урок ${lesson.orderNumber || '?'}</span>
                <a href="tasks.html?lesson=${lesson.id}" class="btn btn-secondary btn-small">Задачи урока</a>
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
        
        // Создаем модальное окно с содержимым урока
        const modal = document.createElement('div');
        modal.className = 'modal';
        modal.innerHTML = `
            <div class="modal-content">
                <span class="modal-close" onclick="this.closest('.modal').remove()">&times;</span>
                <h2>${lesson.title}</h2>
                ${lesson.topic ? `<div class="lesson-topic">Тема: ${lesson.topic}</div>` : ''}
                <div class="lesson-content">${lesson.content || lesson.description || 'Содержимое урока отсутствует'}</div>
                <div class="modal-footer">
                    <button onclick="this.closest('.modal').remove()" class="btn btn-primary">Закрыть</button>
                    <a href="tasks.html?lesson=${lesson.id}" class="btn btn-secondary">Перейти к задачам</a>
                </div>
            </div>
        `;
        document.body.appendChild(modal);
    } catch (error) {
        showMessage('Ошибка при загрузке урока: ' + error.message, 'error');
    }
}

