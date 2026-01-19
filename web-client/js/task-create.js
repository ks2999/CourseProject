// Функции для создания задач

let lessonsList = [];
let skillsList = [];

// Загрузка данных при открытии модального окна
async function openCreateTaskModal() {
    const modal = document.getElementById('create-task-modal');
    if (!modal) return;

    // Загружаем уроки и навыки
    try {
        lessonsList = await lessonApi.getAll();
        skillsList = await skillApi.getAll();

        // Заполняем селекты
        const lessonSelect = document.getElementById('task-lesson-select');
        const skillSelect = document.getElementById('task-skill-select');

        if (lessonSelect) {
            lessonSelect.innerHTML = '<option value="">Не выбран</option>';
            lessonsList.forEach(lesson => {
                const option = document.createElement('option');
                option.value = lesson.id;
                option.textContent = lesson.title;
                lessonSelect.appendChild(option);
            });
        }

        if (skillSelect) {
            skillSelect.innerHTML = '<option value="">Не выбран</option>';
            skillsList.forEach(skill => {
                const option = document.createElement('option');
                option.value = skill.id;
                option.textContent = skill.name;
                skillSelect.appendChild(option);
            });
        }

        modal.style.display = 'block';
    } catch (error) {
        console.error('Ошибка загрузки данных:', error);
        showMessage('Не удалось загрузить уроки и навыки', 'error');
    }
}

function closeCreateTaskModal() {
    const modal = document.getElementById('create-task-modal');
    if (modal) {
        modal.style.display = 'none';
        document.getElementById('create-task-form')?.reset();
    }
}

async function createTask(event) {
    event.preventDefault();

    const userId = getCurrentUserId();
    if (!userId) {
        showMessage('Вы не авторизованы', 'error');
        return;
    }

    const title = document.getElementById('task-title-input').value;
    const description = document.getElementById('task-description-input').value;
    const codeTemplate = document.getElementById('task-code-template').value;
    const difficulty = document.getElementById('task-difficulty-select').value;
    const xpReward = parseInt(document.getElementById('task-xp-reward').value) || 10;
    const lessonId = document.getElementById('task-lesson-select').value || null;
    const skillId = document.getElementById('task-skill-select').value || null;
    const testCases = document.getElementById('task-test-cases').value;
    const published = document.getElementById('task-published').checked;

    // Валидация JSON тестов
    try {
        JSON.parse(testCases);
    } catch (e) {
        showMessage('Неверный формат JSON для тестов', 'error');
        return;
    }

    const taskData = {
        title,
        description: description || null,
        codeTemplate: codeTemplate || null,
        difficulty,
        xpReward,
        lessonId: lessonId || null,
        skillId: skillId || null,
        testCases,
        published
    };

    try {
        await taskApi.create(taskData, userId);
        showMessage('Задача успешно создана!', 'success');
        closeCreateTaskModal();
        
        // Перезагружаем список задач
        if (typeof loadTasks === 'function') {
            await loadTasks();
        }
    } catch (error) {
        console.error('Ошибка создания задачи:', error);
        showMessage(error.message || 'Ошибка при создании задачи', 'error');
    }
}

// Закрытие модального окна при клике вне его
window.addEventListener('click', (event) => {
    const modal = document.getElementById('create-task-modal');
    if (event.target === modal) {
        closeCreateTaskModal();
    }
});

