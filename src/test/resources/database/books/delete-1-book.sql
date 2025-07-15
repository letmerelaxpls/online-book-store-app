DELETE bc
FROM books_categories bc
JOIN books b ON bc.book_id = b.id
WHERE b.title = 'That';

DELETE FROM books WHERE title = 'That';