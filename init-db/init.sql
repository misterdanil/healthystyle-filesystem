--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)
-- Dumped by pg_dump version 16.4 (Ubuntu 16.4-0ubuntu0.24.04.2)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: file; Type: TABLE; Schema: public; Owner: daniel
--

CREATE TABLE public.file (
    id bigint NOT NULL,
    mime_type character varying(255) NOT NULL,
    relative_path character varying(255) NOT NULL,
    root character varying(255) NOT NULL
);


ALTER TABLE public.file OWNER TO daniel;

--
-- Name: file_sequence; Type: SEQUENCE; Schema: public; Owner: daniel
--

CREATE SEQUENCE public.file_sequence
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.file_sequence OWNER TO daniel;

--
-- Data for Name: file; Type: TABLE DATA; Schema: public; Owner: daniel
--

COPY public.file (id, mime_type, relative_path, root) FROM stdin;
1	image/jpeg	articles/02849f17-5ae1-48b9-b684-efdc7c6592e3/7bfc5d08-3581-4683-bccb-d1cab5ac7517.jpg	article
2	image/jpeg	/articles/37/fragments/143/orders/54318a3d-5020-40a1-8c0e-61a25a9ed063/e50c5e80-5b7f-4c3d-a6d4-e8259134ae61.jpg	article
52	text/plain	articles/9e36ce93-41c6-4b0a-9368-1e8e8a6c7fa6/5469e890-2e12-4a3a-81a8-5bc3c8310ae7null	article
53	text/plain	articles/fc8ee747-9fa4-4817-972e-38fe699dc8ee/67509b60-ed47-4965-9853-80bae0e5e308null	article
54	text/plain	articles/4cb1844d-22a6-4b09-ab03-75a535aed42a/c427c9f7-c5c9-4599-9793-6c3b4ebede28null	article
55	text/plain	articles/7fa537e3-7497-4a7c-93e4-63c3577fa2e1/1dcd5c3a-6fe3-472b-83d0-8899bfb32680null	article
56	text/plain	articles/5b39554f-47a6-4683-bd17-6f7962c26b8c/34f24614-a033-4116-8511-f13ffbf2c43fnull	article
57	text/plain	articles/0f22d0a2-050a-4299-9c03-ca807d11221f/751df919-da3c-490b-b3f2-7559221f29a1null	article
58	text/plain	articles/41c95c62-a043-4f73-8088-8284ee171b38/66a5eb6a-05fb-4641-bd9d-74233d2e7f4anull	article
59	text/plain	articles/8c159bed-5702-4078-b60b-7339a436ce98/0acc8f4b-37bc-4198-998f-e86470e39d23null	article
60	text/plain	articles/d97eb757-9b8b-4f70-80ae-5c5903285151/43178ff4-4c5c-43b7-8b46-1bff5fb71548null	article
\.


--
-- Name: file_sequence; Type: SEQUENCE SET; Schema: public; Owner: daniel
--

SELECT pg_catalog.setval('public.file_sequence', 101, true);


--
-- Name: file file_pkey; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- Name: file file_relative_path_key; Type: CONSTRAINT; Schema: public; Owner: daniel
--

ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_relative_path_key UNIQUE (relative_path);


--
-- Name: file_root_idx; Type: INDEX; Schema: public; Owner: daniel
--

CREATE INDEX file_root_idx ON public.file USING btree (root);


--
-- PostgreSQL database dump complete
--

