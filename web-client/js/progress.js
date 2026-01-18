// Загрузка прогресса при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'index.html';
        return;
    }

    await loadProgress(userId);
    await loadSkills(userId);
});

async function loadProgress(userId) {
    try {
        const progress = await progressApi.getProgress(userId);
        
        document.getElementById('total-xp').textContent = progress.totalXp || 0;
        document.getElementById('level').textContent = progress.level || 1;
        document.getElementById('current-streak').textContent = progress.currentStreak || 0;
        document.getElementById('max-streak').textContent = progress.maxStreak || 0;
        document.getElementById('tasks-completed').textContent = progress.tasksCompleted || 0;
        document.getElementById('lessons-completed').textContent = progress.lessonsCompleted || 0;
        
        // Обновляем прогресс-бар
        updateProgressBar(progress.totalXp || 0, progress.level || 1);
    } catch (error) {
        // Если прогресс не найден, создаем его автоматически при первом обращении
        if (error.message.includes('not found')) {
            showMessage('Прогресс будет создан автоматически при первой активности', 'success');
        } else {
            showMessage(error.message || 'Ошибка при загрузке прогресса', 'error');
        }
    }
}

function updateProgressBar(totalXp, level) {
    // Формула: уровень = sqrt(totalXp / 100) + 1
    // Для уровня N нужно (N-1)^2 * 100 XP
    const currentLevelXp = (level - 1) * (level - 1) * 100;
    const nextLevelXp = level * level * 100;
    const xpForCurrentLevel = totalXp - currentLevelXp;
    const xpNeeded = nextLevelXp - currentLevelXp;
    const percentage = Math.min(100, (xpForCurrentLevel / xpNeeded) * 100);
    
    const progressFill = document.getElementById('progress-fill');
    if (progressFill) {
        progressFill.style.width = `${percentage}%`;
    }
    
    const progressText = document.getElementById('progress-text');
    if (progressText) {
        progressText.textContent = `${xpForCurrentLevel} / ${xpNeeded} XP до уровня ${level + 1}`;
    }
}

async function loadSkills(userId) {
    try {
        const skills = await skillApi.getStudentSkills(userId);
        const skillsList = document.getElementById('skills-list');
        
        if (!skillsList) return;
        
        if (skills.length === 0) {
            skillsList.innerHTML = '<p class="loading">Навыки пока не развиты</p>';
            return;
        }
        
        skillsList.innerHTML = skills.map(skill => `
            <div class="skill-item">
                <h4>${skill.skillName}</h4>
                <div class="skill-level">
                    <div>Уровень: ${skill.currentLevel} / ${skill.maxLevel}</div>
                    <div class="skill-level-bar">
                        <div class="skill-level-fill" style="width: ${(skill.currentLevel / skill.maxLevel) * 100}%"></div>
                    </div>
                    <div style="font-size: 0.85em; color: #666; margin-top: 5px;">Опыт: ${skill.experience}</div>
                </div>
            </div>
        `).join('');
    } catch (error) {
        const skillsList = document.getElementById('skills-list');
        if (skillsList) {
            skillsList.innerHTML = '<p class="loading">Навыки пока не развиты</p>';
        }
    }
}

