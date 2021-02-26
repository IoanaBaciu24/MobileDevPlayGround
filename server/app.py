from flask import Flask, jsonify, request
from flask_cors import CORS
from dbmanager import get_all_albums, get_all_pictures_from_album, insert_album, insert_photo, delete_album, delete_photo, update_album_description, update_photo_comment
from entities import Album, Photo

app = Flask(__name__)
app.run(host='0.0.0.0')
CORS(app) 

@app.route("/", methods = ["GET"])
def home():
    return jsonify("Hello, Flask!")


@app.route("/albums", methods = ["GET"])
def get_albums():
    albums = get_all_albums() 
    res = []
    for e in albums:
        res.append(e.toJSON())
    return jsonify(res)

@app.route("/photos/<albumId>", methods = ["GET"])
def get_photos(albumId):
    photos = get_all_pictures_from_album(albumId) 
    res = []
    for e in photos:
        res.append(e.toJSON())
    return jsonify(res)    


@app.route("/albums/post", methods = ["POST"])
def add_album():
    print(request)
    print(request.get_json())
    print(request.get_data())
    name = request.get_json(force = True)["name"] 
    name = "'" + name + "'"
    description = request.get_json(force = True)["description"]
    description = "'" + description + "'"

    print(name) 
    print(description)
    album = insert_album(Album(None, name, description)) 

    return jsonify(album.toJSON())

@app.route("/albums/delete/<albumId>", methods = ["GET"])
def deleteAlbum(albumId):
    delete_album(albumId)
    return jsonify("")


@app.route("/photos/delete/<photoId>", methods = ["GET"])
def deletePhoto(photoId):
    delete_photo(photoId)
    return jsonify("")

@app.route("/albums/update/", methods=["POST"])
def updateAlbum():
    name = request.get_json(force = True)["name"] 
    description = request.get_json(force = True)["description"]
    id = request.get_json(force = True)["id"]
    a = Album(id, name, description)
    name = "'" + name + "'"
    description = "'" + description + "'"
    update_album_description(Album(id, name, description))

    return jsonify(a.toJSON())


@app.route("/photos/update/", methods=["POST"])
def updatePhoto():
    comment = request.get_json(force = True)["comment"] 
    path = request.get_json(force = True)["path"]
    id = request.get_json(force = True)["id"]
    album_id = request.get_json(force = True)["albumId"]
    a = Photo(id, path, comment, album_id)
    comment = "'"+ comment + "'"
    path = "'"+ path + "'"
    update_photo_comment(Photo(id, path,comment, album_id))
    return jsonify(a.toJSON())



@app.route("/photos", methods = ["POST"])
def add_photo():
    comment = request.get_json(force = True)["comment"] 
    comment = "'"+ comment + "'"
    path = request.get_json(force = True)["path"]
    path = "'"+ path + "'"

    album_id = request.get_json(force = True)["albumId"]
    photo = insert_photo(Photo(None, path, comment, album_id))

    return jsonify(photo.toJSON())



