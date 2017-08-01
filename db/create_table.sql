DROP TABLE web_visit;

CREATE TABLE web_visit 
(record_date DATE, 
 web_site VARCHAR(128),
 visit_count BIGINT,
 file_timestamp VARCHAR(14));
 
CREATE INDEX idx_web_visit_vist_count ON web_visit (file_timestamp, record_date, visit_count desc, web_site);

CREATE TABLE web_visit_source_version
(current_file_timestamp VARCHAR(14)) 