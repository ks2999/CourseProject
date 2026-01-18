// API для работы с задачами
const taskApi = {
    async getAll() {
        return api.get('/tasks');
    },

    async getPublished() {
        return api.get('/tasks/published');
    },

    async getById(id) {
        return api.get(`/tasks/${id}`);
    }
};

const submissionApi = {
    async submit(taskId, code, userId) {
        return api.post(`/submissions?userId=${userId}`, { taskId, code });
    },

    async getByUser(userId) {
        return api.get(`/submissions/user/${userId}`);
    },

    async getByTask(taskId) {
        return api.get(`/submissions/task/${taskId}`);
    },

    async getLatest(userId, taskId) {
        return api.get(`/submissions/user/${userId}/task/${taskId}/latest`);
    }
};

let currentTask = null;
let currentTaskId = null;

// Загрузка задач при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'index.html';
        return;
    }

    await loadTasks();
    setupFilters();
});

// Загрузка списка задач
async function loadTasks() {
    try {
        const tasks = await taskApi.getPublished();
        displayTasks(tasks);
    } catch (error) {
        showMessage('Ошибка при загрузке задач: ' + error.message, 'error');
        document.getElementById('tasks-list').innerHTML = '<p class="loading">Ошибка загрузки задач</p>';
    }
}

// Отображение списка задач
function displayTasks(tasks) {
    const tasksList = document.getElementById('tasks-list');
    
    if (tasks.length === 0) {
        tasksList.innerHTML = '<p class="loading">Нет доступных задач</p>';
        return;
    }

    tasksList.innerHTML = tasks.map(task => `
        <div class="task-item" onclick="selectTask('${task.id}')" data-difficulty="${task.difficulty}">
            <div class="task-item-header">
                <div class="task-item-title">${task.title}</div>
                <span class="task-item-difficulty ${task.difficulty.toLowerCase()}">${getDifficultyLabel(task.difficulty)}</span>
            </div>
            <div class="task-item-xp">⭐ ${task.xpReward || 10} XP</div>
        </div>
    `).join('');
}

// Получение метки сложности
function getDifficultyLabel(difficulty) {
    const labels = {
        'EASY': 'Легкая',
        'MEDIUM': 'Средняя',
        'HARD': 'Сложная',
        'EXPERT': 'Экспертная'
    };
    return labels[difficulty] || difficulty;
}

// Настройка фильтров
function setupFilters() {
    document.getElementById('difficulty-filter')?.addEventListener('change', (e) => {
        const selectedDifficulty = e.target.value;
        const taskItems = document.querySelectorAll('.task-item');
        
        taskItems.forEach(item => {
            if (!selectedDifficulty || item.dataset.difficulty === selectedDifficulty) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });
}

// Выбор задачи
async function selectTask(taskId) {
    try {
        // Убираем выделение с других задач
        document.querySelectorAll('.task-item').forEach(item => {
            item.classList.remove('selected');
        });
        
        // Выделяем выбранную задачу
        event.currentTarget.classList.add('selected');
        
        // Загружаем задачу
        const task = await taskApi.getById(taskId);
        currentTask = task;
        currentTaskId = taskId;
        
        displayTask(task);
        await loadSubmissions(taskId);
    } catch (error) {
        showMessage('Ошибка при загрузке задачи: ' + error.message, 'error');
    }
}

// Отображение задачи
function displayTask(task) {
    document.getElementById('no-task-selected').style.display = 'none';
    document.getElementById('task-content').style.display = 'block';
    
    document.getElementById('task-title').textContent = task.title;
    document.getElementById('task-description-text').innerHTML = task.description || 'Описание отсутствует';
    document.getElementById('task-xp-reward').textContent = task.xpReward || 10;
    
    const difficultyEl = document.getElementById('task-difficulty');
    difficultyEl.textContent = getDifficultyLabel(task.difficulty);
    difficultyEl.className = `task-difficulty ${task.difficulty.toLowerCase()}`;
    
    // Устанавливаем шаблон кода
    const codeEditor = document.getElementById('code-editor');
    if (task.codeTemplate) {
        codeEditor.value = task.codeTemplate;
    } else {
        codeEditor.value = '// Напишите ваше решение здесь\n\n';
    }
    
    // Скрываем результаты тестов
    document.getElementById('test-results').style.display = 'none';
}

// Сброс кода
function resetCode() {
    if (currentTask && currentTask.codeTemplate) {
        document.getElementById('code-editor').value = currentTask.codeTemplate;
    } else {
        document.getElementById('code-editor').value = '// Напишите ваше решение здесь\n\n';
    }
    document.getElementById('test-results').style.display = 'none';
}

// Отправка решения
async function submitSolution() {
    const userId = getCurrentUserId();
    if (!userId) {
        showMessage('Пользователь не авторизован', 'error');
        return;
    }
    
    if (!currentTaskId) {
        showMessage('Выберите задачу', 'error');
        return;
    }
    
    const code = document.getElementById('code-editor').value.trim();
    if (!code) {
        showMessage('Напишите код решения', 'error');
        return;
    }
    
    try {
        showMessage('Отправка решения...', 'success');
        
        // TODO: Исправить API - нужно передавать userId как параметр запроса
        const submission = await submissionApi.submit(currentTaskId, code, userId);
        
        displayTestResults(submission);
        await loadSubmissions(currentTaskId);
        
        // Обновляем прогресс
        if (submission.status === 'PASSED' && submission.xpAwarded) {
            showMessage(`Решение принято! Получено ${currentTask.xpReward} XP`, 'success');
        }
    } catch (error) {
        showMessage('Ошибка при отправке решения: ' + error.message, 'error');
    }
}

// Отображение результатов тестов
function displayTestResults(submission) {
    const testResults = document.getElementById('test-results');
    const testResultsContent = document.getElementById('test-results-content');
    
    testResults.style.display = 'block';
    
    let html = '';
    
    // Парсим детальные результаты тестов
    let detailedTests = [];
    if (submission.testResults) {
        try {
            const results = JSON.parse(submission.testResults);
            if (results.tests && Array.isArray(results.tests)) {
                detailedTests = results.tests;
            }
        } catch (e) {
            console.error('Ошибка парсинга результатов тестов:', e);
        }
    }
    
    // Общий статус
    if (submission.status === 'PASSED') {
        html = `
            <div class="test-summary passed">
                <div class="test-result-header">
                    <span class="test-result-name">✅ Все тесты пройдены!</span>
                    <span class="test-result-status passed">ПРОЙДЕНО</span>
                </div>
                <div class="test-result-message">
                    Пройдено тестов: ${submission.testsPassed || 0} / ${submission.testsTotal || 0}
                </div>
            </div>
        `;
    } else if (submission.status === 'FAILED') {
        html = `
            <div class="test-summary failed">
                <div class="test-result-header">
                    <span class="test-result-name">❌ Тесты не пройдены</span>
                    <span class="test-result-status failed">НЕ ПРОЙДЕНО</span>
                </div>
                <div class="test-result-message">
                    Пройдено тестов: ${submission.testsPassed || 0} / ${submission.testsTotal || 0}
                </div>
            </div>
        `;
    } else if (submission.status === 'ERROR') {
        html = `
            <div class="test-summary error">
                <div class="test-result-header">
                    <span class="test-result-name">⚠️ Ошибка компиляции</span>
                    <span class="test-result-status failed">ОШИБКА</span>
                </div>
                <div class="test-result-message">
                    <pre style="white-space: pre-wrap; background: #f8d7da; padding: 10px; border-radius: 5px; margin-top: 10px;">${escapeHtml(submission.errorMessage || 'Ошибка при выполнении кода')}</pre>
                </div>
            </div>
        `;
    } else {
        html = `
            <div class="test-summary">
                <div class="test-result-header">
                    <span class="test-result-name">⏳ На проверке</span>
                    <span class="test-result-status pending">ОЖИДАНИЕ</span>
                </div>
            </div>
        `;
    }
    
    // Детальные результаты каждого теста
    if (detailedTests.length > 0) {
        html += '<div class="detailed-tests"><h4>Детальные результаты тестов:</h4>';
        
        // Если есть ошибка и только один тест - показываем только его
        const showOnlyOne = (submission.status === 'ERROR' && detailedTests.length === 1);
        const testsToShow = showOnlyOne ? detailedTests.slice(0, 1) : detailedTests;
        
        testsToShow.forEach((test, index) => {
            const testClass = test.passed ? 'passed' : 'failed';
            html += `
                <div class="test-detail-item ${testClass}">
                    <div class="test-detail-header">
                        <span class="test-number">Тест ${test.testNumber || (index + 1)}</span>
                        <span class="test-status ${testClass}">${test.passed ? '✅ ПРОЙДЕН' : '❌ НЕ ПРОЙДЕН'}</span>
                    </div>
                    <div class="test-detail-content">
                        <div class="test-io">
                            <strong>Входные данные:</strong>
                            <pre>${escapeHtml(test.input || '(пусто)')}</pre>
                        </div>
                        <div class="test-io">
                            <strong>Ожидаемый вывод:</strong>
                            <pre>${escapeHtml(test.expected || '(пусто)')}</pre>
                        </div>
                        <div class="test-io">
                            <strong>Полученный вывод:</strong>
                            <pre>${escapeHtml(test.actual || '(пусто)')}</pre>
                        </div>
                        ${test.error ? `
                        <div class="test-error">
                            <strong>Ошибка:</strong>
                            <pre>${escapeHtml(test.error)}</pre>
                        </div>
                        ` : ''}
                    </div>
                </div>
            `;
        });
        
        if (showOnlyOne && detailedTests.length > 1) {
            html += `<div class="test-info-note">Показан только первый тест из-за ошибки системы. Всего тестов: ${detailedTests.length}</div>`;
        }
        
        html += '</div>';
    }
    
    testResultsContent.innerHTML = html;
}

function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Загрузка истории решений
async function loadSubmissions(taskId) {
    const userId = getCurrentUserId();
    if (!userId) return;
    
    try {
        const submissions = await submissionApi.getByTask(taskId);
        const userSubmissions = submissions.filter(s => s.userId === userId);
        
        displaySubmissions(userSubmissions);
    } catch (error) {
        console.error('Ошибка при загрузке решений:', error);
    }
}

// Отображение истории решений
function displaySubmissions(submissions) {
    const submissionsList = document.getElementById('submissions-list');
    
    if (submissions.length === 0) {
        submissionsList.innerHTML = '<p class="loading">Пока нет решений</p>';
        return;
    }
    
    submissionsList.innerHTML = submissions.slice(0, 5).map(submission => {
        const date = new Date(submission.createdAt);
        const statusClass = submission.status.toLowerCase();
        const statusLabel = {
            'passed': 'ПРОЙДЕНО',
            'failed': 'НЕ ПРОЙДЕНО',
            'error': 'ОШИБКА',
            'pending': 'ОЖИДАНИЕ'
        }[statusClass] || submission.status;
        
        return `
            <div class="submission-item ${statusClass}">
                <div class="submission-header">
                    <span class="submission-date">${date.toLocaleString('ru-RU')}</span>
                    <span class="submission-status ${statusClass}">${statusLabel}</span>
                </div>
                <div class="submission-stats">
                    Тесты: ${submission.testsPassed || 0} / ${submission.testsTotal || 0}
                    ${submission.xpAwarded ? ' • ⭐ XP получен' : ''}
                </div>
            </div>
        `;
    }).join('');
}

