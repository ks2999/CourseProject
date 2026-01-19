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
        
        // Отображаем аватарку
        if (user.avatar) {
            // Если это относительный путь, добавляем базовый URL
            let avatarUrl = user.avatar;
            if (avatarUrl.startsWith('/api/files/')) {
                avatarUrl = 'http://localhost:8083' + avatarUrl;
            }
            document.getElementById('avatar-img').src = avatarUrl;
        } else {
            document.getElementById('avatar-img').src = 'https://via.placeholder.com/150?text=No+Avatar';
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
async function uploadAvatar(event) {
    const file = event.target.files[0];
    if (!file) return;

    const userId = getCurrentUserId();
    if (!userId) {
        showMessage('Вы не авторизованы', 'error');
        return;
    }

    // Проверяем тип файла
    if (!file.type.startsWith('image/')) {
        showMessage('Файл должен быть изображением', 'error');
        return;
    }

    // Проверяем размер файла (максимум 5MB)
    if (file.size > 5 * 1024 * 1024) {
        showMessage('Размер файла не должен превышать 5MB', 'error');
        return;
    }

    // Показываем превью
    const reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('avatar-img').src = e.target.result;
    };
    reader.readAsDataURL(file);

    // Загружаем файл на сервер
    try {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('userId', userId);

        const response = await fetch(`http://localhost:8083/api/files/avatars?userId=${userId}`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const error = await response.json().catch(() => ({ error: 'Unknown error' }));
            throw new Error(error.error || `HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        const avatarUrl = result.avatarUrl; // Сохраняем относительный путь

        // Обновляем аватарку в профиле пользователя
        await userApi.update(userId, { avatar: avatarUrl });
        
        showMessage('Аватарка успешно загружена!', 'success');
        
        // Обновляем изображение сразу
        document.getElementById('avatar-img').src = 'http://localhost:8083' + avatarUrl;
    } catch (error) {
        console.error('Ошибка загрузки аватарки:', error);
        showMessage(error.message || 'Ошибка при загрузке аватарки', 'error');
        
        // Восстанавливаем старую аватарку
        try {
            const user = await userApi.getById(userId);
            if (user.avatar) {
                let avatarUrl = user.avatar;
                if (avatarUrl.startsWith('/api/files/')) {
                    avatarUrl = 'http://localhost:8083' + avatarUrl;
                }
                document.getElementById('avatar-img').src = avatarUrl;
            } else {
                document.getElementById('avatar-img').src = 'https://via.placeholder.com/150?text=No+Avatar';
            }
        } catch (e) {
            document.getElementById('avatar-img').src = 'https://via.placeholder.com/150?text=No+Avatar';
        }
    }
}

