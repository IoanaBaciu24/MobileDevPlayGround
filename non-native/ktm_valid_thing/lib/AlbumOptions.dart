import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:ktm_valid_thing/EditDecription.dart';
import 'package:ktm_valid_thing/Repository.dart';

import 'Album.dart';

class AlbumOptions extends StatefulWidget {
  Album album;
  AlbumOptions({Key key, @required this.album}) : super(key: key);

  @override
  State<StatefulWidget> createState() => AlbumOptionsState(album);
}

class AlbumOptionsState extends State<AlbumOptions> {
  Album album;
  AlbumOptionsState(this.album);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            alignment: Alignment.centerLeft,
            child: ButtonTheme(
              minWidth: 85.0,
              height: 75.0,
              buttonColor: Colors.deepPurple,
              child: RaisedButton(
                onPressed: () {
                  showAlertDialog(context, album);
                },
                child: Text("Delete this album"),
              ),
            ),
          ),
          Container(
            alignment: Alignment.centerRight,
            child: ButtonTheme(
              minWidth: 85.0,
              height: 75.0,
              buttonColor: Colors.deepPurple,
              child: RaisedButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => EditDescription(
                                album: album,
                              )));
                },
                child: Text("Edit Description"),
              ),
            ),
          )
        ],
      ),
    );
  }
}

void deleteAlbumFromRepo(Album album) {
  Repository repository = Repository();
  repository.deleteAlbum(album);
  print("CAT A RAMAS IN REPO");
  print(repository.items.length);
}

showAlertDialog(BuildContext context, Album album) {
  // set up the buttons
  Widget cancelButton = FlatButton(
    child: Text("Cancel"),
    onPressed: () {},
  );
  Widget continueButton = FlatButton(
    child: Text("Continue"),
    onPressed: () {
      deleteAlbumFromRepo(album);
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
