import 'dart:io';
import 'package:ktm_valid_thing/PhotoDetail.dart';
import 'package:ktm_valid_thing/Repository.dart';
// import 'package:path/path.dart' as path;
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
// import 'package:flutter_absolute_path/flutter_absolute_path.dart';
import 'package:image_picker/image_picker.dart';
import 'package:ktm_valid_thing/AlbumOptions.dart';

import 'Album.dart';

class AlbumDetail extends StatefulWidget {
  Album album;
  AlbumDetail({Key key, @required this.album}) : super(key: key);

  @override
  State<StatefulWidget> createState() => AlbumDetailState(album: album);
}

class AlbumDetailState extends State<AlbumDetail> {
  Album album;
  String _image;
  AlbumDetailState({@required this.album}) : super();

  Future getImage() async {
    // final image = await ImagePicker.pickImage(source: ImageSource.gallery);
    var image =
        await ImagePicker.platform.pickImage(source: ImageSource.gallery);
    if (image != null) {
      setState(() {
        _image = image.path;
      });
    }
    // (context as Element).markNeedsBuild();
  }

  @override
  Widget build(BuildContext context) {
    print(album.description);
    return Scaffold(
        body: Stack(
      children: [
        Container(
            alignment:
                Alignment.lerp(Alignment.topLeft, Alignment.centerRight, 0.3),
            child: Text(
              album.description,
              style: DefaultTextStyle.of(context)
                  .style
                  .apply(fontSizeFactor: 0.5, fontWeightDelta: 1),
            )),
        Container(
          alignment:
              Alignment.lerp(Alignment.topRight, Alignment.centerRight, 0.5),
          child: ButtonTheme(
            minWidth: 85.0,
            height: 75.0,
            buttonColor: Colors.deepPurple,
            child: RaisedButton(
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => AlbumOptions(
                              album: album,
                            ))).then((value) {
                  setState(() {});
                  (context as Element).markNeedsBuild();
                });
              },
              child: Text("Album options"),
            ),
          ),
        ),
        Container(
            alignment:
                Alignment.lerp(Alignment.center, Alignment.bottomCenter, 0.6),
            child: ListView.builder(
              itemCount: album.photos.length,
              shrinkWrap: true,
              scrollDirection: Axis.vertical,
              itemBuilder: (context, index) {
                final List<ListItem> items = List<ListItem>.generate(
                    album.photos.length,
                    (index) => PhotoItem(album.photos[index].path));
                final item = items[index];
                return ListView(
                  shrinkWrap: true,
                  children: [
                    ListTile(
                      title: item.buildName(context),
                      onTap: () {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => PhotoDetail(
                                      album: album,
                                      photo: album.photos[index],
                                    ))).then((value) {
                          setState(() {});
                          (context as Element).markNeedsBuild();
                        });
                      },
                    )
                  ],
                );
              },
            )),
        Container(
          alignment: Alignment.bottomCenter,
          child: ButtonTheme(
            minWidth: 85.0,
            height: 75.0,
            buttonColor: Colors.deepPurple,
            child: RaisedButton(
              onPressed: () {
                getImage();
                // sleep(const Duration(seconds: 4));
                if (_image != null) {
                  addToRepo(album, _image);
                  refreshAlbum();
                  (context as Element).reassemble();
                }
                // if (_image == null)
                //   print("ii null");
                // else {
                //   final path = _image.path;
                //   addToRepo(album, path);
                //   refreshAlbum();
                //   print("ALO IS AICI");
                // }
              },
              child: Text("A"),
            ),
          ),
        ),
      ],
    )
        //la back faci navigater.popcontext -> faci pe buton pe on pressed
        );
  }

  void addToRepo(Album album, String path) {
    Repository repository = Repository();
    repository.addPhoto(album, path);
  }

  void refreshAlbum() {
    Repository repository = Repository();
    for (int i = 0; i < repository.items.length; i++) {
      if (album.name == repository.items[i].name) {
        album = repository.items[i];
        print("FII ANTENA");
        print(album.photos.length);
      }
    }
  }
}

abstract class ListItem {
  Widget buildName(BuildContext context);
  // Widget buildTitle(BuildContext context);
}

class PhotoItem implements ListItem {
  final String path;
  // final Image image

  @override
  Widget buildName(BuildContext context) => Stack(children: [
        //  Image.file(File(coverPhotoPath))
        Container(
            alignment: Alignment.bottomCenter, child: Image.file(File(path))),
      ]);

  // Widget buildTitle(BuildContext context) => Text(name);

  PhotoItem(this.path);
}
