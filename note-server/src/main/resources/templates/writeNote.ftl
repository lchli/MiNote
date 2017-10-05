<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/grids-responsive-min.css">
    <link rel="stylesheet" type="text/css" href="/styles/note-detail.css">
</head>




<script type="text/javascript" >



 window.onbeforeunload= function(){


                                          let oReq = new XMLHttpRequest();
                                          oReq.open("GET", "/deleteNote?noteId="+${noteId}, true);

                                          oReq.send(null);


                                      }



function insertHtml(html,type) {
          var lastMemo = document.getElementById("memo"), lastEditor = document.getElementById("edit");


          type = type || 'memo';

          var control = type == 'memo' ? lastMemo : lastEditor;

          if (!control)return;

          control.focus();

          var selection = window.getSelection ? window.getSelection() : document.selection,

              range = selection.createRange ? selection.createRange() : selection.getRangeAt(0);


          //判断浏览器是ie，但不是ie9以上
          var browser = checkBrowser().split(":");
          var IEbrowser = checkBrowser().split(":")[0];
          var IEverson = Number(checkBrowser().split(":")[1]);

          if (IEbrowser == "IE" && IEverson < 9) {

              range.pasteHTML(html);

          } else {

              var node = document.createElement('span');

              node.innerHTML = html;

              range.insertNode(node);

              selection.addRange(range);

          }
      }


      function getFileName(o) {
                var pos = o.lastIndexOf("\\");
                return o.substring(pos + 1);
            }

          function img() {



              let selectFile = document.getElementById("selectFile");
              let fileName = getFileName(selectFile.value);

              let path="/resources/"+'${noteId}/'+fileName;



              var formdata = new FormData();
              formdata.append("Uid", "${noteId}");
              formdata.append("UserId", "${UserId}");

              formdata.append("files" , selectFile.files[0]);



              let oReq = new XMLHttpRequest();
              oReq.open("POST", "/uploadNote", true);
              oReq.onload = function (oEvent) {
                  selectFile.value="";
                  if (oReq.status == 200) {
                      let responseText = oReq.responseText;
                      console.log(responseText)

                      let img = "<img src=\"" + path + "\" style='width:100%;height:200px;' />";
                      insertHtml(img, 'editor');
                  } else {

                      alert("Error " + oReq.status + " ,uploading your file fail.")
                  }
              };

              oReq.send(formdata);


          }

           function checkBrowser() {

                    var browserName = navigator.userAgent.toLowerCase();
                    //var ua = navigator.userAgent.toLowerCase();
                    var Sys = {};
                    var rtn = false;

                    if (/msie/i.test(browserName) && !/opera/.test(browserName)) {
                        strBrowser = "IE: " + browserName.match(/msie ([\d.]+)/)[1];
                        rtn = true;
                        //return true;
                    } else if (/firefox/i.test(browserName)) {
                        strBrowser = "Firefox: " + browserName.match(/firefox\/([\d.]+)/)[1];
                        ;
                        //return false;
                    } else if (/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName)) {
                        strBrowser = "Chrome: " + browserName.match(/chrome\/([\d.]+)/)[1];
                        //return false;
                    } else if (/opera/i.test(browserName)) {
                        strBrowser = "Opera: " + browserName.match(/opera.([\d.]+)/)[1];
                        //return false;
                    } else if (/webkit/i.test(browserName) && !(/chrome/i.test(browserName) && /webkit/i.test(browserName) && /mozilla/i.test(browserName))) {
                        strBrowser = "Safari: ";
                        //return false;
                    } else {
                        strBrowser = "unKnow,未知浏览器 ";
                        //return false;
                    }
                    strBrowser = strBrowser;
                    //alert(strBrowser)
                    return strBrowser;

                }


                 function save() {

                          let contentEditor = document.getElementById("edit");
                          let titleEditor = document.getElementById("title");
                          let tagEditor = document.getElementById("tag");

                          let title=titleEditor.value;
                          let tag=tagEditor.value;
                          let content=contentEditor.innerHTML;

                          if(title.length==0){
                              alert("标题不能为空！");
                              return;
                          }
                          if (tag.length == 0) {
                              alert("标签不能为空！");
                              return;
                          }
                          if (content.length == 0) {
                              alert("内容不能为空！");
                              return;
                          }

                          var formdata = new FormData();
                          formdata.append("Uid", "${noteId}");
                          formdata.append("UserId", "${UserId}");
                          formdata.append("Content", content);
                          formdata.append("Title", title);
                          formdata.append("Type", tag);


                          let oReq = new XMLHttpRequest();
                          oReq.open("POST", "/uploadNote", true);
                          oReq.onload = function (oEvent) {
                              if (oReq.status == 200) {
                                  let responseText = oReq.responseText;
                                  console.log(responseText);
                                  window.location.href="/noteDetail?Uid=${noteId}";


                              } else {

                                  alert("Error " + oReq.status + " ,save your file fail.")
                              }
                          };

                          oReq.send(formdata);

                      }

                      function insertImg() {
                          var selectFile = document.getElementById("selectFile");
                          selectFile.click();
                      }

</script>

 <label >标题：
        <input type="text" id="title" value="" style="color:#000;width:300px;font-size:16px;">
    </label><br>

    <label >标签：
            <input type="text" id="tag" value="默认" style="color:#000;width:300px;font-size:16px;">
        </label><br>

        <p contentEditable='true' id='edit' style='border:thin solid #000000;min-height:500px;'>

        <div style="margin-top:50px;">

 <input type="button" name="save" value="保存" onclick='save()' style="color:#000;width:300px;height:100px;font-size:48px;">

  <input id='selectFile' type="file" accept="image/*" onchange='img()' style="display:none;">

   <input type="button" value="插入图片" onclick="insertImg()" style="color:#000;width:300px;height:100px;font-size:48px;margin-left:200px;">



</div>
        </p>