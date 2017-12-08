/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var changeDifficulty = function (e) {
    $("form#difficulty").submit();
}

$(document).ready(function () {
    $("form#difficulty").on("change", changeDifficulty);
});


