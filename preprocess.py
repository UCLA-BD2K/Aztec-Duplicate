import locale
import glob
import os.path
import requests
import tarfile
import sys
import codecs
import re
import smart_open

dirname = './'
locale.setlocale(locale.LC_ALL, 'C')

if sys.version > '3':
    control_chars = [chr(0x85)]
else:
    control_chars = [unichr(0x85)]

# Convert text to lower-case and strip punctuation/symbols from words
def normalize_text(text):
    norm_text = text.lower()

    # Replace breaks with spaces
    norm_text = norm_text.replace('<br />', ' ')

    # Pad punctuation with spaces on both sides
    for char in ['.', '"', ',', '(', ')', '!', '?', ';', ':']:
        norm_text = norm_text.replace(char, ' ' + char + ' ')

    return norm_text

import time
start = time.clock()

# if not os.path.isfile('alldata-id.txt'):
if True:
    # Concat and normalize test/train data
    # folders = ['train/pos', 'train/neg', 'test/pos', 'test/neg', 'train/unsup']
    folders = ['train']

    alldata = u''

    for fol in folders:
        temp = u''
        output = fol.replace('/', '-') + '.txt'

        # Is there a better pattern to use?
        txt_files = glob.glob(os.path.join(dirname, fol, '*.txt'))

        for txt in txt_files:
            id = re.findall('\d+', txt)[0]
            with smart_open.smart_open(txt, "rb") as t:
                line = ""
                t_clean = t.read().decode("utf-8")

                for c in control_chars:
                    t_clean = t_clean.replace(c, ' ')

                line += t_clean

            line = line.strip().replace("\n", " ").replace("\r", " ")
            if line:
                temp += "_*" + id + " "
                temp += line
                temp += "\n"

        temp_norm = normalize_text(temp)

        with smart_open.smart_open(os.path.join(dirname, output), "wb") as n:
            n.write(temp_norm.encode("utf-8"))

        alldata += temp_norm

    with smart_open.smart_open(os.path.join(dirname, 'alldata-id.txt'), 'wb') as f:
        for idx, line in enumerate(alldata.splitlines()):
            # num_line = u"_*{0} {1}\n".format(idx, line)
            num_line = u"{0}\n".format(line)
            f.write(num_line.encode("utf-8"))

end = time.clock()
print ("total running time: ", end-start)