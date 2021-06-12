<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title></title>
    <script type="text/javascript">
    $(document).ready(function() {
      $('#inputfile').change(function(){
          var file_data = $('#inputfile').prop('files')[0];
          var form_data = new FormData();
          form_data.append('file', file_data);
          $.ajax({
              url: "pro-img-disk.php", // 요청할 url을 입력
              type: "POST", // 통신 타입, POST or GET
              // async : true, // 비동기 호출 전송 설정 값으로, true/false 를 선택하고 기본 값은 true
              data: form_data, //서버에 요청시 전송할 파라미터
              // dataType : "json", // 응답 받을 데이터의 타입(xml, html, jsom, jsonp, script, text) 선언하지 않으면 default는 서버가 주는 응답의 mimetype을 기본으로 함
              // timeout : 10000, // 요청에 대한 응답 제한 시간으로 단위는 millisecond
              contentType: false, // 서버에 데이터를 보낼 때 형식 지정; default는 "application/x-www-form-urlencoded"
              cache: false,
              processData:false,
              success: function(data){ //HTTP 요청 성공 시 이벤트 핸들러
                  console.log(data);
              }
              // ,
              // error: function(request, status, console.error){ // HTTP 요청 실패 시 이벤트 핸들러
              //
              // },
              // complete: function(){ // HTTP 요청 완료 시 이벤트 핸들러
              //
              // }

          });
      });
    });
    </script>
  </head>
  <body>

    <input type="file" name="inputfile" id = "inputfile">

  </body>
</html>
