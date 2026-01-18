// Загрузка профиля при загрузке страницы
document.addEventListener('DOMContentLoaded', async () => {
    const userId = getCurrentUserId();
    if (!userId) {
        window.location.href = 'index.html';
        return;
    }

    await loadProfile(userId);
});

async function loadProfile(userId) {
    try {
        const user = await userApi.getById(userId);
        
        document.getElementById('user-name').textContent = user.name || '-';
        document.getElementById('user-email').textContent = user.email || '-';
        document.getElementById('user-role').textContent = user.role || '-';
        document.getElementById('user-role-desc').textContent = user.roleDescription || '-';
        
        if (user.avatar) {
            document.getElementById('avatar-img').src = user.avatar;
        }
        
        // Заполняем форму редактирования
        document.getElementById('edit-name').value = user.name || '';
        document.getElementById('edit-email').value = user.email || '';
        document.getElementById('edit-avatar-url').value = user.avatar || '';
    } catch (error) {
        showMessage(error.message || 'Ошибка при загрузке профиля', 'error');
    }
}

// Обновление профиля
document.getElementById('edit-form')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const userId = getCurrentUserId();
    if (!userId) {
        showMessage('Пользователь не авторизован', 'error');
        return;
    }

    const userData = {
        name: document.getElementById('edit-name').value,
        email: document.getElementById('edit-email').value,
        avatar: document.getElementById('edit-avatar-url').value
    };

    try {
        // Временно создаем объект пользователя для проверки прав
        const currentUser = { id: userId };
        await userApi.update(userId, userData);
        showMessage('Профиль успешно обновлен!', 'success');
        await loadProfile(userId);
    } catch (error) {
        showMessage(error.message || 'Ошибка при обновлении профиля', 'error');
    }
});

// Загрузка аватарки
function uploadAvatar(event) {
    const file = event.target.files[0];
    if (!file) return;

    // TODO: Реализовать загрузку файла на сервер
    // Пока просто показываем сообщение
    showMessage('Загрузка файлов пока не реализована. Используйте URL аватарки в форме редактирования.', 'error');
}

