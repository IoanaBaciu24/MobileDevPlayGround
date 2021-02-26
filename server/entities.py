import json


class Album: 
    def __init__(self, id, name, description):
        self.id = id 
        self.name = name 
        self.description = description 


    def toJSON(self):
        res = {}
        res["id"] = self.id
        res["name"] = self.name
        res["description"] = self.description 
        return res


class Photo:
    def __init__(self, id, path, comment, albumId):
        self.id = id 
        self.path = path 
        self.comment = comment 
        self.albumId = albumId 

    def toJSON(self):
        res = {}
        res["id"] = self.id
        res["comment"] = self.comment
        res["path"] = self.path 
        res["albumId"] = self.albumId
        return res
