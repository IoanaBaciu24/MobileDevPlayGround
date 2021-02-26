import psycopg2
from config import config
from typing import List
from entities import Album, Photo


def open_conn():
    """ Connect to the PostgreSQL database server """
    conn = None
    try:
        # read connection parameters
        params = config()

        # connect to the PostgreSQL server
        print('Connecting to the PostgreSQL database...')
        conn = psycopg2.connect(**params)
		
        # create a cursor
        cur = conn.cursor()
        
	# execute a statement
        print('PostgreSQL database version:')
        cur.execute('SELECT version()')

        # display the PostgreSQL database server version
        db_version = cur.fetchone()
        print(db_version)

        # cur.close()
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    return cur,conn


def close_conn(param):
    param.close() 


def get_all_albums():
    cur, conn = open_conn()
    cur.execute("select * from album") 
    all = cur.fetchall()
    result = []
    for entry in all:
        result.append(Album(entry[0], entry[1], entry[2]))
    close_conn(cur)
    close_conn(conn)
    return result

def get_all_pictures_from_album(album_id): 
    cur, conn = open_conn()
    cur.execute("select * from photo where albumId = {0}".format(album_id)) 
    all = cur.fetchall()
    result = []
    for entry in all:
        result.append(Photo(entry[0], entry[1], entry[2], entry[3]))
    close_conn(cur)
    close_conn(conn)
    return result  


def insert_album(album:Album) -> Album:
    cursor,conn = open_conn()
    sql = "insert into album(albumName, albumDescription) values ({0},{1}) RETURNING id".format(album.name, album.description)
    cursor.execute(sql)
    hundred = cursor.fetchone()[0]
    album.id = hundred
    conn.commit()
    close_conn(cursor)
    conn.close()  
    return album  

def insert_photo(photo):
    cursor,conn = open_conn()
    sql = "insert into photo(pathToPicture , comm, albumId) values ({0},{1},{2}) RETURNING id".format(photo.path, photo.comment, photo.albumId)
    cursor.execute(sql)
    hundred = cursor.fetchone()[0]
    photo.id = hundred
    conn.commit()
    close_conn(cursor)
    conn.close()    
    return photo

#TODO update, delete on both entities

def delete_album(album_id: int):
    cursor,conn = open_conn()
    sql = "delete from album where id={0}".format(album_id)
    cursor.execute(sql) 
    conn.commit()
    close_conn(cursor)
    conn.close()

def delete_photo(photo_id: int):
    cursor,conn = open_conn()
    sql = "delete from photo where id={0}".format(photo_id)
    cursor.execute(sql) 
    conn.commit()
    close_conn(cursor)
    conn.close()

def update_album_description(album: Album):
    cursor,conn = open_conn()
    sql = "update album set albumDescription = {0} where id = {1}".format(album.description, album.id)
    cursor.execute(sql) 
    conn.commit()
    close_conn(cursor)
    conn.close()


def update_photo_comment(photo: Photo):
    cursor,conn = open_conn()
    sql = "update photo set comm = {0} where id = {1}".format(photo.comment, photo.id)
    cursor.execute(sql) 
    conn.commit()
    close_conn(cursor)
    conn.close()    

def test_insert():
    insert_album(Album(-1, "'aaa'", "'aaa'"))


if __name__=="__main__":
    # test_insert()
    print(get_all_albums())