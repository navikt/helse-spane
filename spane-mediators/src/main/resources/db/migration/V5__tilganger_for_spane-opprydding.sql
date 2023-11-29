DO
$$
    BEGIN
        IF EXISTS (SELECT FROM pg_roles WHERE rolname = 'spane-opprydding') THEN
            GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "spane-opprydding";
            GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "spane-opprydding";
        END IF;
    END
$$
