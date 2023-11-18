--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 15.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: BayWheelsData; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."BayWheelsData" (
    ride_id character varying(16) NOT NULL,
    rideable_type character varying(16),
    started_at date,
    ended_at date,
    start_station_name character varying(500),
    start_station_id character varying(50),
    end_station_name character varying(500),
    end_station_id character varying(50),
    start_lat double precision,
    start_lng double precision,
    end_lat double precision,
    end_lng double precision,
    member_casual character varying(50)
);


ALTER TABLE public."BayWheelsData" OWNER TO postgres;

--
-- Name: BayWheelsData BayWheelsData_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."BayWheelsData"
    ADD CONSTRAINT "BayWheelsData_pkey" PRIMARY KEY (ride_id);


--
-- PostgreSQL database dump complete
--

