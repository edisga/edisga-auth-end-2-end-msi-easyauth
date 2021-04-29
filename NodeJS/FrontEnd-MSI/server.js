const express = require('express');
const app = express();
const port = process.env.PORT || 3000;
const axios = require('axios');

//variables

var identityEndpoint = process.env.IDENTITY_ENDPOINT;
var identityHeader = process.env.IDENTITY_HEADER;
var resource = process.env.RESOURCE;
var backend_url = process.env.BACKEND_URL;
var api_version = '2019-08-01';

app.get('/', function(req, res, next){

    var url= `${identityEndpoint}?resource=${resource}&api-version=${api_version}`; 

    //Requesting to get MSI token
    axios.get(url, { headers: { 'X-IDENTITY-HEADER': identityHeader }})
    .then((response) => {

        var access_token = response.data.access_token;

        console.log(access_token);

        //Requesting to backend
        axios.get(backend_url, { headers: {'Authorization': `Bearer ${access_token}`}})
        .then((response) => {
            res.json(response.data);
        })
        .catch((error) => {
            console.error(error);
            next();
        })
    })
    .catch((error) => {
        console.error(error)
        res.send(error);
    })
});

app.listen(port, () => console.log(`Example app listening on port ${port}!`))