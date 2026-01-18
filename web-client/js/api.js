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
}

