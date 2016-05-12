from flask import Flask, jsonify, url_for, redirect, request
from flask.ext.pymongo import PyMongo
from flask_restful import Resource, Api
import json

app = Flask(__name__)
#mongo = PyMongo(app)

# connect to another MongoDB server altogether
app.config['MONGO_HOST'] = 'ds017852.mlab.com'
app.config['MONGO_PORT'] = 17852
app.config['MONGO_DBNAME'] = 'project273'
app.config['MONGO_USERNAME'] = 'admin'
app.config['MONGO_PASSWORD'] = 'password'

mongo = PyMongo(app, config_prefix='MONGO')

APP_URL = "http://127.0.0.1:5000"

class Users(Resource):
    def get(self, username=None):
        data = []
        
        if username:
            user_info = mongo.db.user_details.find_one({"id": username}, {"_id": 0, "update_time": 0})

            if user_info:
                return user_info
            else:
                return {"response": "no user found for {}".format(username)}

        else:
            cursor = mongo.db.user_details.find({}, {"_id": 0, "update_time": 0})

            for user in cursor:
                print user
                data.append(user)

            return data

    def post(self):
    	data = []
    	dataStr = request.data
        data = json.loads(dataStr)
        print type(dataStr)
        print type(data)
        
        if not data:
            data = {"response": "ERROR"}
            return jsonify(data)
        else:
            username = data.get('id')
            print "I'm here", username
            if username:
                if mongo.db.user_details.find_one({"id": username}):
                    return {"response": "user already exists."}
                else:
                    result = mongo.db.user_details.insert(data) 
                    return {"status": "ok"} 
            return {"response": "ERROR no username"}

    def put(self, username):
        data = []
        datadic = request.data
        data = json.loads(datadic)
        
        if not data:
            data = {"response": "ERROR"}
            return jsonify(data)
        else:
            username = data.get('id')
            attendance = data.get('class273')
            which_class = data.get('class275')
            print "I'm here in put---", username
            
            if username:
                result = mongo.db.user_details.find_one({"id": username})
                if result:
                    if attendance:
                        mongo.db.user_details.update({'id': username}, {'$push': {'class273': {'$each':attendance}}}, True)
                        return {"status": "upserted ok"}
                    if which_class:
                        mongo.db.user_details.update({'id': username}, {'$push': {'class275': {'$each':which_class}}}, True)                    
                        return {"status": "upserted ok"}
                else:
                    return {"response": "User doesn't exist."}
            return {"response": "ERROR no username"}

    def delete(self,username):
        data = []
        datadic = request.data
        data = json.loads(datadic)
        
        if not data:
            data = {"response": "ERROR"}
            return jsonify(data)
        
        result = mongo.db.user_details.find_one({"id": username})
        wishlist = result['attendance']
        new_wishlist = []
        username = data.get('id')
        sem3id = data.get('sem3id')
        item_flag = 0
        
        print "I'm here in delete---", username
        print "I'm here in delete---", wishlist
        
        if result:
            for item in wishlist:
                if item['sem3id']!= sem3id:
                    print "================================="
                    print "item---", item
                    print "sem3id---", sem3id
                    print "================================="
                    new_wishlist.append(item)
                else:
                    item_flag = 1
        else:
            return {"response": "ERROR no username"}
        
        if item_flag == 1:
            print "I'm here in delete---", new_wishlist
            mongo.db.user_details.update({'user_name': username}, {'$set': {'wish_list': new_wishlist}})
            return {"status":"ok"}
        else:
            return{"status":"Illegal delete. Record not present."}


api = Api(app)

api.add_resource(Users, "/users/<string:username>", endpoint="user_details2")
api.add_resource(Users, "/users/", endpoint="user_details")

if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True)
