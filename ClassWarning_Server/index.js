var mysql = require('mysql');
var http = require('http');
var url = require('url');
var util = require('util');

var connection = mysql.createConnection({
    host: 'localhost',
    user: 'classWarring',
    password: 'classWarring',
    port: '3306',
    database: 'classWarring'
});

connection.connect();

http.createServer(function (req, res) {
    res.writeHead(200, { 'Content-Type': 'text/plain; charset=utf-8' });
    var params = url.parse(req.url, true).pathname;

    switch(params) {
        case "/loginSign":
            var params = url.parse(req.url, true).query;
            var user = params.user;
            // 查询用户密码
            // 使用用户名查询密码
            connection.query("SELECT * FROM UserTable WHERE userName='" + user + "'", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
            break;
        case "/SearchSQL":
            var params = url.parse(req.url, true).query;
            var messagewhat = params.messagewhat;
            var linkID = params.linkid;
            var MysqlID = params.MysqlID;
            var FirstNa = params.FirstNa;
            var queryVar;
            
            // 查询

            switch (messagewhat) {
                case "getClassTitle":
                    queryVar = "SELECT * FROM ClassTable WHERE classID='" + linkID + "'";
                    break;
                case "getMyClass":
                    queryVar = "SELECT * FROM ClassTable WHERE linkID LIKE '%" + linkID + "%'";
                    break;
                case "getAllClassWarning":
                    queryVar = "SELECT * FROM WarringA WHERE classID='" + linkID + "'";
                    break;
                case "getTotalClassWarning":
                    queryVar = "SELECT * FROM ClassTable WHERE classID='" + linkID + "'";
                    break;
                case "getDetailedThings":
                    queryVar = "SELECT * FROM WarringA WHERE id='" + MysqlID + "'";
                    break;
                case "SearchClassWarning":
                    queryVar = "SELECT * FROM WarringA WHERE classID='"+ linkID +"' and WarringStudent LIKE '%"+ FirstNa +"%'";
                    break;
                case "getSchool":
                    queryVar = "SELECT * FROM CooperativeSchool";
                    break;
            }

            connection.query(queryVar, function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
            break;
        case "/AddWarning":
            var params = url.parse(req.url, true).query;
            var Title = params.Title;
            var WarningGroup = params.WarningGroup;
            var WarningStudent = params.WarningStudent;
            var WarningFun = params.WarningFun;
            var FunStartTime = params.FunStartTime;
            var FunEndTime = params.FunEndTime;
            var BeizhuSS = params.BeizhuSS;
            var ee = params.ee;
            var LinkID = params.linkid;
            var Total = params.Total;
            var classID = params.classID;
    
            connection.query("INSERT INTO WarringA VALUES (null,'" + Title + "','" + WarningGroup + "','" + WarningStudent + 
            "','"+ WarningFun + "','" + FunStartTime + "','" + FunEndTime +"','" + BeizhuSS +"','" + ee +"','" + LinkID + "','" + classID +"')", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
            connection.query("UPDATE ClassTable SET warningTotal="+ Total +" WHERE classID='" + classID + "'", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
            break;
        case "/addUser":
            var params = url.parse(req.url, true).query;
            var user = params.user;
            var password = params.password;
            var linkid = params.linkid;
            var schoolName = params.schoolName;
            var grade = params.grade;
            var worker = params.worker;
    
            connection.query("INSERT INTO UserTable VALUES (null, '" + user + "','" + password + "','" + linkid + "','" + schoolName + "','" + grade
            + "','" + worker +"')", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
            });
            connection.query("INSERT INTO WarningTotal VALUES ('0','" + linkid + "')", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                res.end("注册成功!");
            });
            break;
        case "/joinClassReyly":
            var params = url.parse(req.url, true).query;
            var inGrade = params.inGrade;
            var className = params.className;
            var linkid = params.linkid;
    
            connection.query("INSERT INTO ClassTable VALUES (null, '" + "null" + "','" + inGrade + "','" + className + "','" + "0" + "','" + linkid +"')", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                res.end("创建成功!");
            });
            break;
        case "/jionClassReply":
            var params = url.parse(req.url, true).query;
            var classID = params.classID;
            var linkid = params.linkid;

            connection.query("UPDATE ClassTable SET linkID=CONCAT(linkID,';" + linkid + "') WHERE classID=" + classID, function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                res.end("加入成功!");
            });
            break;
        default:
            res.end("未定义的GET请求！");
            break;
    }
}).listen(2563);