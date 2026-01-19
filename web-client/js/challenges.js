// Загрузка и отображение соревнований

async function loadChallenges() {
    try {
        const challenges = await challengeApi.getAll();
        displayChallenges(challenges);
    } catch (error) {
        console.error('Ошибка загрузки соревнований:', error);
        showMessage('Не удалось загрузить соревнования', 'error');
    }
}

function displayChallenges(challenges) {
    const container = document.getElementById('challenges-container');
    if (!container) return;

    if (challenges.length === 0) {
        container.innerHTML = '<p class="no-data">Соревнований пока нет</p>';
        return;
    }

    container.innerHTML = challenges.map(challenge => {
        const isActive = challenge.isCurrentlyActive;
        const startDate = new Date(challenge.startDate).toLocaleDateString('ru-RU');
        const endDate = new Date(challenge.endDate).toLocaleDateString('ru-RU');
        const tasksCount = challenge.tasks ? challenge.tasks.length : 0;
        
        return `
            <div class="challenge-card ${isActive ? 'active' : ''}">
                <div class="challenge-header">
                    <h3>${escapeHtml(challenge.title)}</h3>
                    <span class="challenge-type">${escapeHtml(challenge.typeDescription)}</span>
                </div>
                <p class="challenge-description">${escapeHtml(challenge.description || '')}</p>
                <div class="challenge-info">
                    <div class="info-item">
                        <span class="info-label">📅 Период:</span>
                        <span>${startDate} - ${endDate}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">📝 Задач:</span>
                        <span>${tasksCount}</span>
                    </div>
                    <div class="info-item">
                        <span class="info-label">⭐ Награда:</span>
                        <span>${challenge.xpReward} XP</span>
                    </div>
                    ${challenge.createdByName ? `
                    <div class="info-item">
                        <span class="info-label">👤 Создатель:</span>
                        <span>${escapeHtml(challenge.createdByName)}</span>
                    </div>
                    ` : ''}
                </div>
                ${challenge.tasks && challenge.tasks.length > 0 ? `
                <div class="challenge-tasks">
                    <strong>Задачи:</strong>
                    <ul>
                        ${challenge.tasks.map(task => `
                            <li>
                                <a href="tasks.html?taskId=${task.id}" class="task-link">
                                    ${escapeHtml(task.title)} 
                                    <span class="difficulty-badge ${task.difficulty.toLowerCase()}">${task.difficulty}</span>
                                </a>
                            </li>
                        `).join('')}
                    </ul>
                </div>
                ` : ''}
                <div class="challenge-status">
                    ${isActive ? 
                        '<span class="status-badge active">🟢 Активно</span>' : 
                        '<span class="status-badge inactive">⚪ Неактивно</span>'
                    }
                </div>
            </div>
        `;
    }).join('');
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Загружаем соревнования при загрузке страницы, если пользователь авторизован
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (userId) {
        // Загружаем информацию о пользователе для получения роли
        await loadCurrentUser();
        
        const challengesSection = document.getElementById('challenges-section');
        const registerSection = document.getElementById('register-section');
        const loginSection = document.getElementById('login-section');
        const createBtn = document.getElementById('create-challenge-btn');
        
        if (challengesSection) {
            challengesSection.style.display = 'block';
            if (registerSection) registerSection.style.display = 'none';
            if (loginSection) loginSection.style.display = 'none';
            
            // Показываем кнопку создания только для учителей
            if (createBtn && isTeacher()) {
                createBtn.style.display = 'inline-block';
            }
            
            loadChallenges();
        }
    }
});

