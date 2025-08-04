INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES
  (4, 'The Shining', 'Stephen King', '9780307743657', 19.99, 'Classic horror novel', NULL, false),
  (5, 'That', 'Stephen King', '9781501142970', 22.99, 'Another horror classic', NULL, false),
  (6, 'The Hitchhikers Guide to the Galaxy', 'Douglas Adams', '9780345391803', 15.50, 'Sci-fi comedy', NULL, false);

INSERT INTO books_categories (book_id, category_id) VALUES (4, 4),
 (5, 4),
 (6, 5);
