DO
$$
    BEGIN
        IF EXISTS (SELECT FROM pg_roles WHERE rolname = 'spane-opprydding') THEN
            GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "spedisjon-opprydding";
            GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "spedisjon-opprydding";
        END IF;
    END
$$
