from gensim.models import Word2Vec
from gensim.models import Doc2Vec
from gensim.models.doc2vec import TaggedDocument
from nltk import word_tokenize
import numpy as np
from scipy import spatial
import re
import operator
from nltk.stem.wordnet import WordNetLemmatizer
lmtzr = WordNetLemmatizer()
from nltk.stem.porter import PorterStemmer
porter_stemmer = PorterStemmer()

def tag_doc():
    f = open('alldata-id.txt','r')
    n_dimension = 400
    taggeddoc = []
    ids = []
    line = f.readline()
    index = 0
    while line != '':
        line = line.replace('\n','')
        line = ' '.join(word_tokenize(line.decode('utf8')))
        line = line.encode('utf8').strip().split(' ')
        id = line[0]
        id = re.findall('\d+', id)[0]
        line = line[1:]
        td = TaggedDocument(line,['tool'+str(id)])
        taggeddoc.append(td)
        ids.append(id)
        line = f.readline()
        index += 1
    f.close()
    print "data loaded"
    return ids, index, taggeddoc
ids, num, taggeddoc = tag_doc()
# num = 10801

def build_model(tagd):
    print "length of doc: ",len(tagd)
    model = Doc2Vec(tagd, size=400, window=5, min_count=1, workers=20, alpha=0.025, min_alpha=0.025)
    model.save('doc2vec_model_400')
    print "model saved"
    return model
model = build_model(taggeddoc)

model_name = 'doc2vec_model_400'
def save_vectors(model_name, ids):
    print "to be continue"
    model = Doc2Vec.load(model_name)
    f = open('DocVectors.csv','w')
    for id in ids:
        docvec = model.docvecs['tool'+str(id)]
        docvec = np.insert(docvec, 0, id)
        f.write(','.join(str(x) for x in docvec)+'\n')
    f.close()
save_vectors(model_name, ids)

import math
def cosine_similarity(v1,v2):
    "compute cosine similarity of v1 to v2: (v1 dot v2)/{||v1||*||v2||)"
    sumxx, sumxy, sumyy = 0, 0, 0
    for i in range(len(v1)):
        x = v1[i]; y = v2[i]
        sumxx += x*x
        sumyy += y*y
        sumxy += x*y
    return sumxy/math.sqrt(sumxx*sumyy)

def similarity(name):
    f = open(name, 'r')
    lines = f.readlines()
    num = len(lines)
    similar = np.zeros((num + 1, num + 1))
    map = {}
    counter = 0
    for line in lines:
        split_line = line.strip().split(',')
        float_arr = [float(n) for n in split_line]
        id = int(float_arr[0])
        vecs = float_arr[1:]
        similar[0][counter + 1] = id
        similar[counter + 1][0] = id
        map[id] = vecs
        counter += 1

    for i in range(1, num + 1):
        print str(i)
        for j in range(i, num + 1):
            id_i = similar[0][i]
            id_j = similar[j][0]
            vec_i = map[id_i]
            vec_j = map[id_j]
            similar[i][j] = cosine_similarity(vec_i, vec_j)
    similar = np.around(similar, 4)
    np.savetxt("similarity.csv", similar, delimiter=",")

similarity('DocVectors.csv')


def calculate(sim_file, id_file):
    output = "calculated_" + id_file
    print 'loading similar'
    similar = np.loadtxt(sim_file, delimiter=",")
    print 'loading ids'
    ids = np.loadtxt(id_file, delimiter=",")
    map = {}
    f = open(sim_file, 'r')
    line = f.readline()
    split_line = line.strip().split(',')
    float_arr = [float(n) for n in split_line]

    index = 0
    for id in float_arr:d_
        id_int = int(id)
        map[id_int] = index
        index += 1

    final = np.zeros((ids.shape[0], 3))
    for i in range(ids.shape[0]):
        id_one = ids[i][0]
        id_two = ids[i][1]
        calculated = check_similarity(similar, id_one, id_two, map)
        final[i][0] = id_one
        final[i][1] = id_two
        final[i][2] = calculated
    np.savetxt(output, final, delimiter=",")

def check_similarity(similar, id_one, id_two, map):
    index_one = map.get(id_one)
    index_two = map.get(id_two)
    if index_one == None or index_two == None:
        return -1
    if index_one > index_two:
        temp = index_one
        index_one = index_two
        index_two = temp
    return similar[index_one][index_two]

calculate('similarity.csv', 'similar_author.csv')
