UPDATE tasks
SET title = ?,
    description = ?,
    completed = ?
WHERE id = ?
  AND user_id = ?;
