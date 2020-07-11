var mysql = require('mysql');
const http = require('http');
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
    if (params == "/loginSign") {
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
    } else if (params == "/SearchSQL") {
        var params = url.parse(req.url, true).query;
        var messagewhat = params.messagewhat;
        var linkID = params.linkid;
        var MysqlID = params.MysqlID;
        var FirstNa = params.FirstNa;
        
        // 查询

        if (messagewhat == "0") {
            connection.query("SELECT * FROM WarringA WHERE LinkID='" + linkID + "'", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
        } else if (messagewhat == "1") {
            connection.query("SELECT * FROM WarningTotal WHERE LinkID='" + linkID + "'", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
            
        } else if (messagewhat == "2") {
            connection.query("SELECT * FROM WarringA WHERE id=" + MysqlID, function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
        } else if (messagewhat == "3") {
            connection.query("SELECT * FROM WarringA WHERE LinkID='"+ linkID +"' and WarringStudent LIKE '%"+ FirstNa +"%'", function (err, result) {
                if (err) {
                    console.log('[SELECT ERROR] - ', err.message);
                    res.write(FirstNa);
                    res.end("SQL语句错误！");
                }
                var str = JSON.stringify(result);
                res.end(str);
            });
        }
        
    } else if (params == "/AddWarning") {
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

        connection.query("INSERT INTO WarringA  VALUES (null,'" + Title + "','" + WarningGroup + "','" + WarningStudent + 
        "','"+ WarningFun + "','" + FunStartTime + "','" + FunEndTime +"','" + BeizhuSS +"','" + ee +"','" + LinkID +"')", function (err, result) {
            if (err) {
                console.log('[SELECT ERROR] - ', err.message);
                res.end("SQL语句错误！");
            }
            var str = JSON.stringify(result);
            res.end(str);
        });
        connection.query("UPDATE WarningTotal SET WarningTotal="+ Total +" WHERE LinkID='" + LinkID + "'", function (err, result) {
            if (err) {
                console.log('[SELECT ERROR] - ', err.message);
                res.end("SQL语句错误！");
            }
            var str = JSON.stringify(result);
            res.end(str);
        });
    } else if (params == "/addUser") {
        var params = url.parse(req.url, true).query;
        var user = params.user;
        var password = params.password;
        var linkid = params.linkid;
        var howToCall = params.howToCall;

        connection.query("INSERT INTO UserTable VALUES (null, '" + user + "','" + password + "','" + howToCall + "','"+ linkid + "')", function (err, result) {
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
    } else {
        res.end("未定义的GET请求！")
    }
}).listen(2563);