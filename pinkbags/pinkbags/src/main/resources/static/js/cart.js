document.addEventListener('DOMContentLoaded', function () {
    const updateElements = document.querySelectorAll('.update_cart');

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    updateElements.forEach(el => {
        el.addEventListener('click', function(e) {
            e.preventDefault();

            const productId = el.dataset.product;
            const action = el.dataset.action || 'add';

            fetch('/update-item', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ productId: productId, action: action }),
                credentials: 'same-origin'
            })
            .then(res => {
                if (!res.ok) {
                    if (res.status === 401 || res.status === 403) {
                        // User not authenticated
                        window.location.href = '/login';
                    }
                    throw new Error('Network error');
                }
                return res.text();
            })
            .then(data => {
                console.log('Server response:', data);

                // Update cart counter (replace with your counter selector)
                const cartCounter = document.querySelector('#cart-count');
                if (cartCounter) {
                    let count = parseInt(cartCounter.textContent) || 0;
                    if (action === 'add') count += 1;
                    else if (action === 'remove' && count > 0) count -= 1;
                    cartCounter.textContent = count;
                }

                // Optional visual feedback
                el.classList.add('clicked');
                setTimeout(() => el.classList.remove('clicked'), 300);
            })
            .catch(err => console.error(err));
        });
    });
});
