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
    if (params == "/runSQL") {
        var params = url.parse(req.url, true).query;
        var sql = params.sql;
        // 查
        connection.query(sql, function (err, result) {
            if (err) {
                console.log('[SELECT ERROR] - ', err.message);
                res.end("SQL运行错误！")
                return;
            }
            var str = JSON.stringify(result);
            res.end(str);
        });
    } else {
        res.end("未定义的GET请求！")
    }
}).listen(2563);