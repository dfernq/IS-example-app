# is-example-app

## Database management

Docker util commands:
- Check if database is up and running:
  ```bash
  docker exec -it user_management_db mysql -u root -proot -e "SHOW DATABASES;"
  ```

- Clean volumes to reset database state:
  ```bash
  docker-compose down -v
  docker-compose up --build
  ```