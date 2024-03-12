create definer = root@localhost trigger set_gameID_equal_to_id
    before insert
    on games
    for each row
# BEGIN
    SET new.gameID = new.id;
# END;

