function executeKill() {
    alert("hello");
    var killId=[[${detail.id}]];
    var userId=10;
    alert(killId,userId)
    $.ajax({
        type: "POST",
        url: "kill/execute",
        contentType: "application/json;charset=utf-8",
        killId:killId,
        userId:userId,
        dataType: "json",

        success: function(res){
            if (res.code==0) {
                alert("抢购成功")
                //alert(res.msg);
                window.location.href="kill/execute/success"
            }else{
                alert("抢购失败")
                //alert(res.msg);
                window.location.href="kill/execute/fail"
            }
        },
        error: function (message) {
            alert("提交数据失败！");
            return;
        }
    });
}

// function getJsonData() {
//     alert("oooo");
//     var killId=[[${detail.id}]];
//     /*var data = {
//         "killId":killId,
//         "userId":1
//     };*/
//     alert(killId);
//     var data = {
//         "killId":killId
//     };
//     return data;
// }
