require('dotenv').config()
const express = require('express')
const bodyParser = require('body-parser');
const app = express()
app.use(bodyParser());
const port = process.env.PORT
const request = require('request');
var admin = require("firebase-admin");
var serviceAccount = require('./service-account.json');
var cron = require('node-cron');
var async = require("async");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://capital-one-19531.firebaseio.com"
});

var db = admin.database();


// Charity Endpoints //
app.get('/v0/charity/getCharities', function (req, res) {
    var options = { method: 'GET',
    url: 'https://api.data.charitynavigator.org/v2/Organizations',
    qs: 
     { app_id: process.env.CHARITY_APP_ID,
       app_key: process.env.CHARITY_APP_KEY,
       pageSize: 5,
       rated: true,
       sort:"RATING:DESC"
     }
    };
    request(options, function (error, response, body) {
    if (error) throw new Error(error);  
    res.send(JSON.parse(body))
  });
})

app.post('/v0/charity/updatePref', function (req, res) {
    var charities = req.body.charity_id
    var blacklisted = req.body.blacklisted
    var firebaseUserID = req.body._id
    admin.database().ref('/users/' + firebaseUserID + '/pref/').update({
        blacklisted: blacklisted,
        charities: charities
    })
    res.status(200).send({ status: "success!" });
})

app.get('/v0/charity/getPref', function(req, res) {
    var firebaseUserID = req.query._id
    db.ref("/users/" + firebaseUserID + "/pref").on("value", function(snapshot) {
        res.status(200).send(snapshot.val())
    })
})

app.post('/v0/charity/createDonationAccount', function (req, res) {
    db.ref('/users/' + req.query.uid).once('value').then(function(snapshot) {
        if(snapshot.exists()) {
            res.send(JSON.parse('{"error": "Account already has donation account"}'))
        } else {
            var options = { 
                method: 'POST',
                url: 'http://api.reimaginebanking.com/customers/'+process.env.CAP_CUST_ID+'/accounts',
                qs: { key: process.env.CAP_ONE },
                body: { 
                    type: 'Savings',
                    nickname: 'Savings account towards charities',
                    rewards: 0,
                    balance: 0 
                },
                json: true 
            };
            request(options, function (error, response, body) {
            if (error) throw new Error(error);
            admin.database().ref('/users/' + req.query.uid).update({
                doesHaveDonationAccount: true
            })
            res.send(body)
            });
        }
    })
})

app.get('/v0/charity/getAccounts', function(req, res) {
    var options = { method: 'GET',
    url: 'http://api.reimaginebanking.com/accounts',
    qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' }};

    request(options, function (error, response, body) {
    if (error) throw new Error(error);
    res.status(200).send(JSON.parse(body))
    });
})

function updateTransactionHistories(firebaseUserID, capCustomerAccount) {
    var options = { method: 'GET',
    url: 'http://api.reimaginebanking.com/accounts',
    qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' }};

    var total = 0

    var options = { method: 'GET',
    url: 'http://api.reimaginebanking.com/accounts',
    qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' }};

    request(options, function (error, response, body) {
        if (error) throw new Error(error);
        for(var i = 0; i < JSON.parse(body).length; i++) {
            total += JSON.parse(body)[i].rewards / 100
        }
    });

    request(options, function (error, response, body) {
        if (error) throw new Error(error);
        var accountsJson = JSON.parse(body)
        async.forEachOf(accountsJson, (value, i, callback) => {
            var options = { method: 'GET',
            url: 'http://api.reimaginebanking.com/accounts/'+accountsJson[i]._id+'/purchases',
            qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' }};
            var accountID = accountsJson[i]._id
            request(options, function (purchaseError, purchaseResponse, purchaseBody) {
                if (purchaseError) throw new Error(purchaseError);
                var parsedPurchaseBody = JSON.parse(purchaseBody);
                for(var j in parsedPurchaseBody) {
                    var firebaseUserID = process.env.FIREBASE_TEST_ACC
                    var charityValue = 1 - (parsedPurchaseBody[j].amount % 1)
                    db.ref('/users/' + firebaseUserID + '/transactions/'+accountID+ "/"+parsedPurchaseBody[j]._id).set({
                        _id:parsedPurchaseBody[j]._id,
                        amount:parsedPurchaseBody[j].amount,
                        charity: charityValue,
                        account: accountID,
                        merchant_id: parsedPurchaseBody[j].merchant_id
                    })
                    total += Math.ceil(charityValue * 100) / 100;
                }
                callback();
            })
        }, err => {
            db.ref('/users/' + firebaseUserID + '/transactions/totalCharity').set(total)
        });
    })
}

cron.schedule('* * * * *', () => {
    updateTransactionHistories(process.env.FIREBASE_TEST_ACC, process.env.CAP_CUST_ACC)
});

app.get('/v0/charity/getTransactions', function (req, res) {
    var accountNumber = req.query.accountNumber
    var firebaseUserID = process.env.FIREBASE_TEST_ACC
    var test = []
    if(accountNumber == "") {
        res.send('you fucked up')
    }
    db.ref('/users/' + firebaseUserID + '/transactions/'+accountNumber).on("value", function(snapshot) {
        for (const [key, value] of Object.entries(snapshot.val())) {
            test.push(value)
        }
        res.status(200).send(test)
    })
})

app.get('/v0/charity/totalCharity', function(req, res) {
    var firebaseUserID = process.env.FIREBASE_TEST_ACC
    db.ref('/users/' + firebaseUserID + '/transactions/totalCharity').on("value", function(snapshot) {
        res.send(JSON.stringify({a: snapshot.val}))
    })
})

app.get('/v0/charity/updateTransactions', function (req, res) {
    res.send(updateTransactionHistories(process.env.FIREBASE_TEST_ACC, process.env.CAP_CUST_ACC))
})

app.get('/v0/charity/getMerchants', function (req, res) {
    var merchantID = req.query.merchantID
    
    var options = { method: 'GET',
    url: 'http://api.reimaginebanking.com/merchants/'+merchantID,
    qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' }};

    request(options, function (error, response, body) {
        res.send(JSON.parse(body))
    });

})
//// GEOFENCING STUFF
app.get('/v0/geofence/getPlaces', function (req, res) {
  const type = req.headers.type || "";
  const location = req.headers.location || "";
  if (type == "" || location == "") {
    return res.error("Not all parameters provided");
  }
  var options = {
    method: 'GET',
    url: 'https://maps.googleapis.com/maps/api/place/nearbysearch/json',
    qs:
    {
      key: process.env.GOOGLE_MAPS_KEY,
      radius: 5000,
      location: location,
      type: type
    }
  };
  request(options, function (error, response, body) {
    if (error) throw new Error(error);
    let locations = [];
    const results = JSON.parse(body)['results'];
    for (let i = 0; i < results.length; ++i) {
      locations.push({
        lat: results[i]['geometry']['location']['lat'],
        lng: results[i]['geometry']['location']['lng'],
        name: results[i]['place_id']
      })
    }
    return res.send({ locations: locations });
  });
});

app.post('/v0/geofence/createGeofence', function(req, res){
    const reqArray = req.body
    var i
    for(i in reqArray) {
        const latitude = reqArray[i].latitude || ""
        const longtitude = reqArray[i].longtitude || ""
        const radius = reqArray[i].radius || ""
        const expTime = reqArray[i].exp || ""
        const key = reqArray[i].key || ""
        console.log(reqArray[i])
        if(expTime == "" || radius == "" || longtitude == "" || latitude == "" || key == "") {
            continue;
        }
        db.ref("/users/" + process.env.FIREBASE_TEST_ACC + "/geofence/" + key).set({
            latitude: latitude,
            longtitude: longtitude,
            radius: radius,
            expTime: expTime,
            key: key
        })
    }
    res.status(200).send("")
  });

app.get('/v0/geofence/getGeofences', function(req, res) {
    var firebaseUserID = process.env.FIREBASE_TEST_ACC
    db.ref('/users/' + firebaseUserID + '/geofence/').on("value", function(snapshot) {
        res.status(200).send(snapshot.val())
    })
})

app.post('/v0/geofence/setCardStatus', function(req, res) {
    var firebaseUserID = process.env.FIREBASE_TEST_ACC
    const status = req.body.status || ""
    if(status == "") {
        res.status(422).send('{"error": "missing the thing brother"}')
        return
    }
    db.ref('/users/' + firebaseUserID + "/isCardDisabled").set(status)
    res.status(200).send("")
})

app.get('/v0/geofence/getCardStatus', function(req, res) {
    var firebaseUserID = process.env.FIREBASE_TEST_ACC
    db.ref('/users/' + firebaseUserID + '/isCardDisabled').on("value", function(snapshot) {
        res.status(200).send(snapshot.val())
    })
})
app.listen(port, () => console.log(`Trash State School Coder server running on: ${port}!`))
