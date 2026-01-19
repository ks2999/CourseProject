// Конфигурация API
const API_BASE_URL = 'http://localhost:8083/api';

// Утилиты для работы с API
const api = {
    async request(url, options = {}) {
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, {
                ...options,
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                }
            });

            if (!response.ok) {
                const error = await response.json().catch(() => ({ error: 'Unknown error' }));
                throw new Error(error.error || `HTTP error! status: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    },

    async get(url) {
        return this.request(url, { method: 'GET' });
    },

    async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    async put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },

    async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
};

// Функции для работы с пользователями
const userApi = {
    async create(userData) {
        return api.post('/users', userData);
    },

    async getById(id) {
        return api.get(`/users/${id}`);
    },

    async getAll() {
        return api.get('/users');
    },

    async update(id, userData) {
        return api.put(`/users/${id}`, userData);
    },

    async delete(id) {
        return api.delete(`/users/${id}`);
    }
};

// Функции для работы с файлами
const fileApi = {
    async uploadAvatar(file, userId) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('userId', userId);
        
        const response = await fetch(`${API_BASE_URL}/files/avatars?userId=${userId}`, {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            const error = await response.json().catch(() => ({ error: 'Unknown error' }));
            throw new Error(error.error || `HTTP error! status: ${response.status}`);
        }
        
        return await response.json();
    },
    
    getAvatarUrl(filename) {
        return `${API_BASE_URL}/files/avatars/${filename}`;
    }
};

// Функции для работы с прогрессом
const progressApi = {
    async getProgress(userId) {
        return api.get(`/progress/user/${userId}`);
    },

    async getLeaderboard() {
        return api.get('/progress/leaderboard');
    }
};

// Функции для работы с навыками
const skillApi = {
    async getStudentSkills(userId) {
        return api.get(`/student-skills/user/${userId}`);
    },

    async getAll() {
        return api.get('/skills');
    },

    async getById(id) {
        return api.get(`/skills/${id}`);
    }
};

// Функции для работы с уроками
const lessonApi = {
    async getAll() {
        return api.get('/lessons');
    },

    async getPublished() {
        return api.get('/lessons/published');
    },

    async getById(id) {
        return api.get(`/lessons/${id}`);
    }
};

// Функции для работы с задачами
const taskApi = {
    async getAll() {
        return api.get('/tasks');
    },

    async getPublished() {
        return api.get('/tasks/published');
    },

    async getById(id) {
        return api.get(`/tasks/${id}`);
    },

    async create(taskData, createdById) {
        return api.post(`/tasks?createdById=${createdById}`, taskData);
    },

    async update(id, taskData, currentUserId) {
        return api.put(`/tasks/${id}?currentUserId=${currentUserId}`, taskData);
    },

    async delete(id, currentUserId) {
        return api.delete(`/tasks/${id}?currentUserId=${currentUserId}`);
    }
};

// Функции для работы с соревнованиями
const challengeApi = {
    async getAll() {
        return api.get('/challenges');
    },

    async getActive() {
        return api.get('/challenges/active');
    },

    async getById(id) {
        return api.get(`/challenges/${id}`);
    },

    async create(challengeData, createdById) {
        return api.post(`/challenges?createdById=${createdById}`, challengeData);
    },

    async update(id, challengeData, currentUserId) {
        return api.put(`/challenges/${id}?currentUserId=${currentUserId}`, challengeData);
    },

    async delete(id, currentUserId) {
        return api.delete(`/challenges/${id}?currentUserId=${currentUserId}`);
    }
};

// Утилиты
function showMessage(text, type = 'success') {
    const messageEl = document.getElementById('message');
    if (messageEl) {
        messageEl.textContent = text;
        messageEl.className = `message ${type}`;
        messageEl.style.display = 'block';
        
        setTimeout(() => {
            messageEl.style.display = 'none';
        }, 5000);
    }
}

function getCurrentUserId() {
    return localStorage.getItem('userId');
}

function setCurrentUser(userId) {
    localStorage.setItem('userId', userId);
}

function clearCurrentUser() {
    localStorage.removeItem('userId');
    localStorage.removeItem('userRole');
}

// Сохранение информации о пользователе
async function loadCurrentUser() {
    const userId = getCurrentUserId();
    if (!userId) return null;
    
    try {
        const user = await userApi.getById(userId);
        if (user && user.role) {
            localStorage.setItem('userRole', user.role);
        }
        return user;
    } catch (error) {
        console.error('Ошибка загрузки пользователя:', error);
        return null;
    }
}

// Проверка роли пользователя
function isTeacher() {
    const role = localStorage.getItem('userRole');
    return role === 'TEACHER' || role === 'ADMIN';
}

function getCurrentUserRole() {
    return localStorage.getItem('userRole');
}

