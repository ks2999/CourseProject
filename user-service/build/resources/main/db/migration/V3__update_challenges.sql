-- Обновление таблицы challenges для поддержки нескольких задач
-- Создаем промежуточную таблицу для связи Many-to-Many

CREATE TABLE IF NOT EXISTS challenge_tasks (
    challenge_id UUID NOT NULL REFERENCES challenges(id) ON DELETE CASCADE,
    task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    PRIMARY KEY (challenge_id, task_id)
);

-- Добавляем поле created_by если его еще нет
DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'challenges' AND column_name = 'created_by'
    ) THEN
        ALTER TABLE challenges ADD COLUMN created_by UUID REFERENCES users(id);
    END IF;
END $$;

-- Создаем индекс для улучшения производительности
CREATE INDEX IF NOT EXISTS idx_challenge_tasks_challenge ON challenge_tasks(challenge_id);
CREATE INDEX IF NOT EXISTS idx_challenge_tasks_task ON challenge_tasks(task_id);
CREATE INDEX IF NOT EXISTS idx_challenges_created_by ON challenges(created_by);

