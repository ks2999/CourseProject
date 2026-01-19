// Функции для создания соревнований

let allTasksList = [];
let selectedTaskIds = [];

// Загрузка задач при открытии модального окна
async function openCreateChallengeModal() {
    const modal = document.getElementById('create-challenge-modal');
    if (!modal) return;

    try {
        // Загружаем все задачи
        allTasksList = await taskApi.getAll();
        
        // Устанавливаем текущую дату и время по умолчанию
        const now = new Date();
        const startDate = new Date(now.getTime() + 60 * 60 * 1000); // +1 час
        const endDate = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000); // +7 дней

        document.getElementById('challenge-start-date').value = formatDateTimeLocal(startDate);
        document.getElementById('challenge-end-date').value = formatDateTimeLocal(endDate);

        // Отображаем список задач
        displayTasksForSelection(allTasksList);
        selectedTaskIds = [];

        modal.style.display = 'block';
    } catch (error) {
        console.error('Ошибка загрузки задач:', error);
        showMessage('Не удалось загрузить задачи', 'error');
    }
}

function formatDateTimeLocal(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

function displayTasksForSelection(tasks) {
    const container = document.getElementById('tasks-selection-list');
    if (!container) return;

    if (tasks.length === 0) {
        container.innerHTML = '<p class="no-data">Задач не найдено</p>';
        return;
    }

    container.innerHTML = tasks.map(task => {
        const isSelected = selectedTaskIds.includes(task.id);
        return `
            <div class="task-selection-item ${isSelected ? 'selected' : ''}" onclick="toggleTaskSelection('${task.id}')">
                <input type="checkbox" ${isSelected ? 'checked' : ''} onchange="toggleTaskSelection('${task.id}')">
                <div class="task-selection-info">
                    <strong>${escapeHtml(task.title)}</strong>
                    <span class="task-meta-small">
                        <span class="difficulty-badge ${task.difficulty.toLowerCase()}">${task.difficulty}</span>
                        <span>⭐ ${task.xpReward} XP</span>
                    </span>
                </div>
            </div>
        `;
    }).join('');
}

function toggleTaskSelection(taskId) {
    const index = selectedTaskIds.indexOf(taskId);
    if (index > -1) {
        selectedTaskIds.splice(index, 1);
    } else {
        selectedTaskIds.push(taskId);
    }
    
    updateSelectedTasksDisplay();
    displayTasksForSelection(allTasksList);
}

function updateSelectedTasksDisplay() {
    const container = document.getElementById('selected-tasks-list');
    if (!container) return;

    if (selectedTaskIds.length === 0) {
        container.innerHTML = '<p class="no-data">Задачи не выбраны</p>';
        return;
    }

    const selectedTasks = allTasksList.filter(task => selectedTaskIds.includes(task.id));
    container.innerHTML = selectedTasks.map(task => `
        <div class="selected-task-item">
            <span>${escapeHtml(task.title)}</span>
            <button type="button" class="btn-remove" onclick="removeTaskFromSelection('${task.id}')">×</button>
        </div>
    `).join('');
}

function removeTaskFromSelection(taskId) {
    const index = selectedTaskIds.indexOf(taskId);
    if (index > -1) {
        selectedTaskIds.splice(index, 1);
        updateSelectedTasksDisplay();
        displayTasksForSelection(allTasksList);
    }
}

function filterTasks() {
    const searchTerm = document.getElementById('task-search').value.toLowerCase();
    const filtered = allTasksList.filter(task => 
        task.title.toLowerCase().includes(searchTerm)
    );
    displayTasksForSelection(filtered);
}

function closeCreateChallengeModal() {
    const modal = document.getElementById('create-challenge-modal');
    if (modal) {
        modal.style.display = 'none';
        document.getElementById('create-challenge-form')?.reset();
        selectedTaskIds = [];
        allTasksList = [];
    }
}

async function createChallenge(event) {
    event.preventDefault();

    const userId = getCurrentUserId();
    if (!userId) {
        showMessage('Вы не авторизованы', 'error');
        return;
    }

    if (selectedTaskIds.length === 0) {
        showMessage('Выберите хотя бы одну задачу', 'error');
        return;
    }

    const title = document.getElementById('challenge-title-input').value;
    const description = document.getElementById('challenge-description-input').value;
    const type = document.getElementById('challenge-type-select').value;
    const xpReward = parseInt(document.getElementById('challenge-xp-reward').value) || 50;
    const startDate = new Date(document.getElementById('challenge-start-date').value);
    const endDate = new Date(document.getElementById('challenge-end-date').value);

    if (endDate <= startDate) {
        showMessage('Дата окончания должна быть позже даты начала', 'error');
        return;
    }

    const challengeData = {
        title,
        description: description || null,
        type,
        taskIds: selectedTaskIds,
        startDate: startDate.toISOString(),
        endDate: endDate.toISOString(),
        xpReward,
        active: true
    };

    try {
        await challengeApi.create(challengeData, userId);
        showMessage('Соревнование успешно создано!', 'success');
        closeCreateChallengeModal();
        
        // Перезагружаем список соревнований
        if (typeof loadChallenges === 'function') {
            await loadChallenges();
        }
    } catch (error) {
        console.error('Ошибка создания соревнования:', error);
        showMessage(error.message || 'Ошибка при создании соревнования', 'error');
    }
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Закрытие модального окна при клике вне его
window.addEventListener('click', (event) => {
    const modal = document.getElementById('create-challenge-modal');
    if (event.target === modal) {
        closeCreateChallengeModal();
    }
});

