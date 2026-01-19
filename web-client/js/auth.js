// Проверка авторизации при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    
    if (window.location.pathname.includes('index.html')) {
        // На главной странице
        if (userId) {
            // Пользователь авторизован - показываем соревнования
            await loadCurrentUser();
            
            const challengesSection = document.getElementById('challenges-section');
            const registerSection = document.getElementById('register-section');
            const loginSection = document.getElementById('login-section');
            const nav = document.getElementById('nav');
            
            if (challengesSection) {
                challengesSection.style.display = 'block';
                if (registerSection) registerSection.style.display = 'none';
                if (loginSection) loginSection.style.display = 'none';
                
                // Показываем навигацию
                if (nav) {
                    const profileLink = document.getElementById('profile-link');
                    const progressLink = document.getElementById('progress-link');
                    const lessonsLink = document.getElementById('lessons-link');
                    const tasksLink = document.getElementById('tasks-link');
                    const achievementsLink = document.getElementById('achievements-link');
                    const logoutBtn = document.getElementById('logout-btn');
                    
                    if (profileLink) profileLink.style.display = 'inline';
                    if (progressLink) progressLink.style.display = 'inline';
                    if (lessonsLink) lessonsLink.style.display = 'inline';
                    if (tasksLink) tasksLink.style.display = 'inline';
                    if (achievementsLink) achievementsLink.style.display = 'inline';
                    if (logoutBtn) logoutBtn.style.display = 'inline';
                }
            }
        } else {
            // Пользователь не авторизован - показываем форму входа
            const challengesSection = document.getElementById('challenges-section');
            const registerSection = document.getElementById('register-section');
            const loginSection = document.getElementById('login-section');
            const nav = document.getElementById('nav');
            
            if (challengesSection) challengesSection.style.display = 'none';
            if (registerSection) registerSection.style.display = 'none';
            if (loginSection) loginSection.style.display = 'block';
            
            // Скрываем навигацию для неавторизованных
            if (nav) {
                const profileLink = document.getElementById('profile-link');
                const progressLink = document.getElementById('progress-link');
                const lessonsLink = document.getElementById('lessons-link');
                const tasksLink = document.getElementById('tasks-link');
                const achievementsLink = document.getElementById('achievements-link');
                const logoutBtn = document.getElementById('logout-btn');
                
                if (profileLink) profileLink.style.display = 'none';
                if (progressLink) progressLink.style.display = 'none';
                if (lessonsLink) lessonsLink.style.display = 'none';
                if (tasksLink) tasksLink.style.display = 'none';
                if (achievementsLink) achievementsLink.style.display = 'none';
                if (logoutBtn) logoutBtn.style.display = 'none';
            }
        }
    } else {
        // На других страницах - редирект на главную, если не авторизован
        if (!userId) {
            window.location.href = 'index.html';
        }
    }
});

// Переключение между формами
function showLogin() {
    document.getElementById('register-section').style.display = 'none';
    document.getElementById('login-section').style.display = 'block';
}

function showRegister() {
    document.getElementById('login-section').style.display = 'none';
    document.getElementById('register-section').style.display = 'block';
}

// Регистрация
document.getElementById('register-form')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const userData = {
        name: document.getElementById('reg-name').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value
    };

    try {
        const response = await userApi.create(userData);
        showMessage('Регистрация успешна! Теперь войдите в систему.', 'success');
        setTimeout(() => showLogin(), 2000);
    } catch (error) {
        showMessage(error.message || 'Ошибка при регистрации', 'error');
    }
});

// Вход
document.getElementById('login-form')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;

    try {
        // TODO: Реализовать реальную аутентификацию через auth-service
        // Пока просто ищем пользователя по email
        const users = await userApi.getAll();
        const user = users.find(u => u.email === email);
        
        if (user) {
            setCurrentUser(user.id);
            if (user.role) {
                localStorage.setItem('userRole', user.role);
            }
            showMessage('Вход выполнен успешно!', 'success');
            
            // Если на главной странице, показываем соревнования
            if (window.location.pathname.includes('index.html')) {
                setTimeout(() => {
                    location.reload(); // Перезагружаем страницу для показа соревнований
                }, 1000);
            } else {
                setTimeout(() => {
                    window.location.href = 'profile.html';
                }, 1000);
            }
        } else {
            showMessage('Пользователь не найден', 'error');
        }
    } catch (error) {
        showMessage(error.message || 'Ошибка при входе', 'error');
    }
});

// Выход
function logout() {
    clearCurrentUser();
    window.location.href = 'index.html';
}

