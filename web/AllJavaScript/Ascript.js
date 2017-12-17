/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function (){
    $('#uploadFiles').on('change', function(){
        var divSubmit = $('#divSubmit');

        var uploadFiles = document.getElementById('uploadFiles');
        var text = "";
        console.log(uploadFiles.files);
        if('files' in uploadFiles && uploadFiles.files.length>0){
            console.log("True" + uploadFiles.files.length);
            var file = uploadFiles.files;
            for(var i = 0; i < file.length; i++){
                text += "<br><b>" + (i+1) + "</b>. File";
                text += "name: " + file[i].name;
                text += "<br>File size: " + file[i].size + "bytes";
            }
            $('#deleteAll').show();
        }
        else{
           $('#deleteAll').hide();
//            document.getElementById("deleteAll").style.visibility = "hidden";
            console.log("False");
            
        }
        document.getElementById("fileInfo").innerHTML = text;
    });
    $('#deleteAll').on('click', function(){
       this.form.reset();
       $('#deleteAll').hide();
       document.getElementById("fileInfo").innerHTML = "";
       console.log(uploadFiles.files);
    });
});

$(document).ready(function () {
    $('#submit').on('click', function () {
//        var uploadFiles = document.getElementById('uploadFiles');
        var form = $('#uploadFiles')[0].files;

        if (form.length > 0) {
            console.log("Length: " +form.length);
            for (var i = 0; i < form.length; i++) {
                var formData = new FormData();
                formData.append('file', form[i]);
                $.ajax({
                    url: 'FileInfoServlet',
                    enctype: 'multipart/form-data',
                    type: 'POST',
                    processData: false,
                    contentType: false,
                    data: formData,
                    success: function (responseText) {
                        $('#outputMsg').text(responseText);
                        $("#submit").prop("disabled", false);
                    },
                    error: function (e) {
                        $('#outputMsg').text(e.responseText);
                        $("#submit").prop("disabled", false);
                    }
                });
            }
        }

    });
});
