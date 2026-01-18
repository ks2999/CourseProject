// Загрузка достижений при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'index.html';
        return;
    }

    await loadAchievements(userId);
    await loadLeaderboard();
});

async function loadAchievements(userId) {
    try {
        // TODO: Реализовать API для получения бейджей пользователя
        // Пока показываем заглушку
        const badgesGrid = document.getElementById('badges-grid');
        if (badgesGrid) {
            badgesGrid.innerHTML = `
                <div class="badge-card">
                    <div class="badge-icon">🎯</div>
                    <div class="badge-name">Первая задача</div>
                    <div class="badge-date">Получено: -</div>
                </div>
                <div class="badge-card">
                    <div class="badge-icon">🔥</div>
                    <div class="badge-name">Серия дней</div>
                    <div class="badge-date">Получено: -</div>
                </div>
                <div class="badge-card">
                    <div class="badge-icon">⭐</div>
                    <div class="badge-name">Повышение уровня</div>
                    <div class="badge-date">Получено: -</div>
                </div>
            `;
        }
    } catch (error) {
        showMessage(error.message || 'Ошибка при загрузке достижений', 'error');
    }
}

async function loadLeaderboard() {
    try {
        const leaderboard = await progressApi.getLeaderboard();
        const leaderboardEl = document.getElementById('leaderboard');
        
        if (!leaderboardEl) return;
        
        if (leaderboard.length === 0) {
            leaderboardEl.innerHTML = '<p class="loading">Лидерборд пуст</p>';
            return;
        }
        
        leaderboardEl.innerHTML = leaderboard.slice(0, 10).map((user, index) => `
            <div class="leaderboard-item">
                <div class="leaderboard-rank ${index < 3 ? 'top' : ''}">${index + 1}</div>
                <div class="leaderboard-info">
                    <div class="leaderboard-name">${user.userName || 'Пользователь'}</div>
                    <div class="leaderboard-stats">Уровень ${user.level} • ${user.tasksCompleted || 0} задач</div>
                </div>
                <div class="leaderboard-xp">${user.totalXp || 0} XP</div>
            </div>
        `).join('');
    } catch (error) {
        const leaderboardEl = document.getElementById('leaderboard');
        if (leaderboardEl) {
            leaderboardEl.innerHTML = '<p class="loading">Ошибка при загрузке лидерборда</p>';
        }
    }
}

