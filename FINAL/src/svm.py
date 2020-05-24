import inline as inline
import matplotlib
import sklearn
import pandas as pd
from sklearn.metrics import classification_report, confusion_matrix
import numpy as np
import matplotlib.pyplot as plt
#%matplotlib inline
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split

data = pd.read_csv("svm/svm.csv")

data.shape
data.head()
X = data.drop('Class', axis=1)
y = data['Class']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.5)
svclassifier = SVC(kernel='linear')
svclassifier.fit(X_train, y_train)
y_pred = svclassifier.predict(X_test)

print(confusion_matrix(y_test,y_pred))
print(classification_report(y_test,y_pred))