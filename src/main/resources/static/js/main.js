// Auto-dismiss the "added to cart" flash message after a few seconds.
document.addEventListener('DOMContentLoaded', function () {
    var flash = document.querySelector('.flash-message');
    if (flash) {
        setTimeout(function () {
            flash.style.transition = 'opacity 0.4s ease';
            flash.style.opacity = '0';
            setTimeout(function () {
                flash.remove();
            }, 400);
        }, 3000);
    }
});
