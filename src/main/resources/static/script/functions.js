function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
})

var count=1;
document.getElementById("addRow").addEventListener("click", function() {
    document.getElementById("ingredientContainer").append(addNewRow(count));
    count++;
});

function addNewRow(count){
    var newrow='<div className="col-md-2">'
        + '<label htmlFor="validationServer04" className="form-label">Einheit '+count+'</label>'
        +'<select name="unit '+count+'" type="text" className="form-select">'

        +'</select>'
        +'</div>'
        +'<div className="col-md-3">'
        + '<label htmlFor="validationDefault03" className="form-label">Menge '+count+'</label>'
        +'<input name="quantity '+count+'" type="text" className="form-control" id="validationDefault03" required>'
        +'</div>'
        +'<div className="col-md-7">'
        + '<label htmlFor="validationDefault03" className="form-label">Zutat '+count+'</label>'
        +'<input name="ingredient '+count+'" type="text" className="form-control" id="validationDefault03" required>'
        +'</div>'
    return newrow;
}