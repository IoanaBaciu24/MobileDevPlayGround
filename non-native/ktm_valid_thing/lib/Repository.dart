import 'package:ktm_valid_thing/Album.dart';
import 'package:ktm_valid_thing/Photo.dart';

class Repository {
  // Repository._private_Co

  // static final Repository _repository = Repository;

  // List<Album> items;

  // factory Repository() {
  //   return _repository;
  // }

  // Repository() {
  //   this.items = List();
  //   this.items.add(
  //       Album("ALO PRONTO", "b", "/storage/emulated/0/Download/IMG_1641.jpg"));
  //   this.items.add(Album(
  //       "alo pronto 2", "b", "/storage/emulated/0/Download/IMG_1641.jpg"));
  // }

  Repository._privateConstructor(this.items);

  static Repository _instance = Repository._privateConstructor(List<Album>());
  List<Album> items = List<Album>();
  factory Repository() {
    if (_instance.items.isEmpty) {
      List<Photo> photos = List();
      photos.add(
          Photo("p1", "ma vezi?", "/storage/emulated/0/Download/IMG_1641.jpg"));
      _instance.items.add(Album("ALO PRONTO", "b", photos));
    }
    return _instance;
  }

  void addPhoto(Album a, String p) {
    for (int i = 0; i < items.length; i++) {
      if (a.name == items[i].name) {
        items[i].photos.add(Photo("", "", p));
        return;
      }
    }
  }

  void deleteAlbum(Album album) {
    items.removeWhere((item) =>
        item.name == album.name && item.description == album.description);
  }

  void updateAlbumDesc(Album a, String description) {
    for (int i = 0; i < items.length; i++) {
      if (a.name == items[i].name) {
        items[i].description = description;
        return;
      }
    }
  }

  void deletePhotoFromAlbum(Album album, Photo photo) {
    for (int i = 0; i < items.length; i++) {
      if (album.name == items[i].name) {
        items[i].photos.removeWhere((element) => element.path == photo.path);
      }
    }
  }
}
