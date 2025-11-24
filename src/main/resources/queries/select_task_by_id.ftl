SELECT id,
       title,
       description,
       completed,
       user_id AS userId
FROM tasks
WHERE id = ?;
