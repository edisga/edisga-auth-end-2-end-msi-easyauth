const express = require('express');
const app = express();
const port = process.env.PORT || 3000;

var items =[{ 'name': 'Item1', 'name': 'Item2'}];

app.get('/api', async function(req, res){
    res.json(items);
});

app.listen(port, () => console.log(`Example app listening on port ${port}!`))