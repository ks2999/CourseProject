/**
 * Простой парсер Markdown для красивого отображения теории
 */
function parseMarkdown(text) {
    if (!text) return '';
    
    let html = text;
    
    // Заголовки
    html = html.replace(/^### (.*$)/gim, '<h3>$1</h3>');
    html = html.replace(/^## (.*$)/gim, '<h2>$1</h2>');
    html = html.replace(/^# (.*$)/gim, '<h1>$1</h1>');
    
    // Код блоки (```code``` или ```lang code```)
    html = html.replace(/```(\w+)?\n?([\s\S]*?)```/g, function(match, lang, code) {
        const language = lang ? lang.toLowerCase() : 'c';
        return '<pre class="code-block"><code class="language-' + language + '">' + 
               escapeHtml(code.trim()) + '</code></pre>';
    });
    
    // Инлайн код (`code`)
    html = html.replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>');
    
    // Жирный текст (**text**)
    html = html.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
    
    // Курсив (*text*)
    html = html.replace(/\*(.+?)\*/g, '<em>$1</em>');
    
    // Списки (начинающиеся с - или цифры)
    // Сначала обрабатываем нумерованные списки
    html = html.replace(/^(\d+)\.\s+(.+)$/gim, '<li class="numbered">$2</li>');
    // Затем маркированные
    html = html.replace(/^-\s+(.+)$/gim, '<li class="bulleted">$1</li>');
    
    // Обернуть последовательные <li> в <ol> или <ul>
    html = html.replace(/(<li[^>]*>.*?<\/li>\s*)+/g, function(match) {
        // Проверяем, есть ли numbered в классе
        if (match.includes('numbered')) {
            return '<ol>' + match.replace(/class="numbered"/g, '') + '</ol>';
        } else if (match.includes('bulleted')) {
            return '<ul>' + match.replace(/class="bulleted"/g, '') + '</ul>';
        }
        return match;
    });
    
    // Параграфы (двойной перевод строки)
    const lines = html.split('\n');
    let result = [];
    let currentPara = [];
    
    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim();
        
        if (!line) {
            if (currentPara.length > 0) {
                const paraText = currentPara.join(' ');
                if (paraText && !paraText.match(/^<(h[1-6]|pre|ul|ol|blockquote|p)/)) {
                    result.push('<p>' + paraText + '</p>');
                } else {
                    result.push(paraText);
                }
                currentPara = [];
            }
            continue;
        }
        
        // Если строка уже обработана (заголовок, код, список)
        if (line.match(/^<(h[1-6]|pre|ul|ol|li|blockquote)/)) {
            if (currentPara.length > 0) {
                const paraText = currentPara.join(' ');
                if (paraText) {
                    result.push('<p>' + paraText + '</p>');
                }
                currentPara = [];
            }
            result.push(line);
        } else {
            currentPara.push(line);
        }
    }
    
    // Обработать оставшийся параграф
    if (currentPara.length > 0) {
        const paraText = currentPara.join(' ');
        if (paraText && !paraText.match(/^<(h[1-6]|pre|ul|ol|blockquote|p)/)) {
            result.push('<p>' + paraText + '</p>');
        } else {
            result.push(paraText);
        }
    }
    
    html = result.join('\n');
    
    // Убрать пустые параграфы
    html = html.replace(/<p>\s*<\/p>/g, '');
    
    return html;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

