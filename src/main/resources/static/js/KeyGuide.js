document.addEventListener('DOMContentLoaded', function () {
    var tocSubject = document.querySelector('.toc__subject');

    tocSubject.addEventListener('click', function () {
        this.classList.toggle('opened'); // Toggle the 'opened' class on click

        // Next, we toggle the visibility of the next sibling element, which should be the '.toc__contents'
        var tocContents = this.nextElementSibling;
        if (this.classList.contains('opened')) {
            tocContents.style.maxHeight = tocContents.scrollHeight + "px";
        } else {
            tocContents.style.maxHeight = null;
        }
    });
});
