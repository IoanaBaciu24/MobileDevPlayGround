import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:ktm_valid_thing/Photo.dart';
import 'package:ktm_valid_thing/Repository.dart';

import 'Album.dart';

class PhotoDetail extends StatefulWidget {
  Photo photo;
  Album album;
  PhotoDetail({Key key, @required this.photo, @required this.album})
      : super(key: key);

  @override
  State<StatefulWidget> createState() => PhotoDetailState(photo, album);
}

class PhotoDetailState extends State<PhotoDetail> {
  Photo photo;
  Album album;

  PhotoDetailState(this.photo, this.album);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
              alignment: Alignment.topRight,
              child: ButtonTheme(
                minWidth: 85.0,
                height: 75.0,
                buttonColor: Colors.deepPurple,
                child: RaisedButton(
                  onPressed: () {
                    showAlertDialog(context, album, photo);
                  },
                  child: Text("Delete photo"),
                ),
              )),
          Container(
            alignment: Alignment.center,
            child: Image.file(File(photo.path)),
          )
        ],
      ),
    );
  }
}

void showAlertDialog(BuildContext context, Album album, Photo photo) {
  // set up the buttons
  Widget cancelButton = FlatButton(
    child: Text("Cancel"),
    onPressed: () {},
  );
  Widget continueButton = FlatButton(
    child: Text("Continue"),
    onPressed: () {
      Repository repository = Repository();
      repository.deletePhotoFromAlbum(album, photo);
    },
  );
  // set up the AlertDialog
  AlertDialog alert = AlertDialog(
    title: Text("AlertDialog"),
    content: Text("Are you sure you want to delete this album?"),
    actions: [
      cancelButton,
      continueButton,
    ],
  );
  // show the dialog
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return alert;
    },
  );
}
