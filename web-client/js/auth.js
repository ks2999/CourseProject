// Проверка авторизации при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    const userId = getCurrentUserId();
    if (userId && window.location.pathname.includes('index.html')) {
        window.location.href = 'profile.html';
    } else if (!userId && !window.location.pathname.includes('index.html')) {
        window.location.href = 'index.html';
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
            showMessage('Вход выполнен успешно!', 'success');
            setTimeout(() => {
                window.location.href = 'profile.html';
            }, 1000);
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

