Download new set of data from Solr.

Run main.java to create mappings.
	- Change id on line 355 in BaseCases.java to updated id
	- Final results are in all_name_combined_tools.json and all_name_mapping.csv.
	- Train folder should be filled with data

Run preprocess.py to set up for doc2vec.
Run doc2vec_train.py to train doc2vec and get vectors.

Notes: If changes are made to the metadata, the combination function should be updated. Currently matching on names; update code block at line 44 on Main.java if different criteria wanted.
