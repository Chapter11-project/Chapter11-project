$(function () {
    let index = 0;
    const slideCount = 4;
    const slideWidth = 100;

    setInterval(function () {
        index++;

        if (index === slideCount) {
            index = 0;
        }

        $(".slidelist").css(
            "margin-left",
            -(index * slideWidth) + "%"
        );
    }, 3000);
});
