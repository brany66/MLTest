## -*- coding: utf-8 -*-
# @Time    : 18-5-2 下午9:41
# @Author  : gallup
# @Email   : gallup-liu@hotmail.com
# @File    : model_v_X.py
# @Software: PyCharm

import lightgbm as lgb
import pandas as pd
from math import log
import numpy as np
from sklearn.model_selection import train_test_split


def metric(y_pred, y_true):
    length = len(y_pred)
    assert length == len(y_true)

    sum = 0
    for i in range(length):
        value = round(y_pred[i], 3)
        y_pred_log = log(value + 1)
        y_true_log = log(y_true[i] + 1)
        sum += pow((y_pred_log - y_true_log), 2)
    return sum / length


def my_cost_function(preds, train_data):
    labels = train_data.get_label()
    grad = []
    for i in range(len(labels)):
        labelsLog = log(labels[i] + 1)
        predsLog = log(preds[i] + 1)
        grad.append((predsLog - labelsLog)/(preds[i] + 1))

    hess = []
    for i in range(len(labels)):
        labelsLog = log(labels[i] + 1)
        predsLog = log(preds[i] + 1)
        hess.append((1 + labelsLog - predsLog)/pow(preds[i] + 1, 2))

    return grad, hess


def my_cost(preds, train_data):
    labels = train_data.get_label()
    cost = 0
    for i in range(len(labels)):
        labelsLog = log(labels[i] + 1)
        predsLog = log(preds[i] + 1)
        cost += pow((labelsLog - predsLog), 2)

    return 'error', cost, False

# 将训练集分割为训练集和测试集
def splitTrainData(features, labels):

    df_train_features, df_eval_features, df_train_label, df_eval_label = \
        train_test_split(features, labels, test_size=0.2)

    return df_train_features, df_eval_features, df_train_label, df_eval_label


def train_model_level1(features, labels, evalFeatures, evalLabel):

    lgb_train = lgb.Dataset(features, label=labels)
    lgb_eval = lgb.Dataset(evalFeatures, label=evalLabel, reference=lgb_train)

    # specify your configurations as a dict
    params = {
        'task': 'train',
        'boosting_type': 'rf',
        'objective': 'regression',
        'metric': {'l2', 'rmse'},
        'num_leaves': 40,
        'max_depth': 6,
        'min_data_in_leaf': 12,
        'learning_rate': 0.015,

        'feature_fraction': 0.8,
        'bagging_fraction': 0.8,

        'num_iterations': 1200,
        # 'n_estimators': 106,

        'bagging_freq': 10,
        'verbose': 0,
    }

    print('Start training...')
    mLevel1 = lgb.train(params,
                        lgb_train,
                        num_boost_round=80,
                        valid_sets=lgb_eval,
                        early_stopping_rounds=20)

    return mLevel1


def train_model_level2(features, labels, evalFeatures, evalLabel):

    lgb_train = lgb.Dataset(features, label=labels)
    lgb_eval = lgb.Dataset(evalFeatures, label=evalLabel, reference=lgb_train)

    params = {
        'task': 'train',
        'boosting_type': 'gbdt',
        'objective': 'regression',
        'metric': {'l2', 'rmse'},
        'num_leaves': 40,
        'max_depth': 6,
        'min_data_in_leaf': 12,
        'learning_rate': 0.015,

        'feature_fraction': 0.8,
        'bagging_fraction': 0.8,

        'num_iterations': 1200,
        # 'n_estimators': 106,

        'bagging_freq': 10,
        'verbose': 0,
    }

    print('Start training...')
    mLevel2 = lgb.train(params,
                        lgb_train,
                        num_boost_round=80,
                        valid_sets=lgb_train,
                        early_stopping_rounds=20)

    return mLevel2

if __name__ == "__main__":
    # load or create your dataset
    print('Load data...')
    df_train_data = pd.read_csv('/Users/zhangtai01/PycharmProjects/tainchi/model_data.1.2.2/train.1.2.2/part-00000', header=None,
                                encoding='utf-8')
    df_test_data = pd.read_csv('/Users/zhangtai01/PycharmProjects/tainchi/model_data.1.2.2/test.1.2.2/part-00000', header=None,
                               encoding='utf-8')
    df_features = df_train_data.iloc[:, 1:104].values
    df_labels = df_train_data.iloc[:, 104:109]

    df_test_features = df_test_data.iloc[:, 1:104].values

    print(df_features.shape[0], df_labels.shape[0], df_features.shape[1])
    print(df_features.shape[1], df_test_features.shape[1])
    assert df_features.shape[0] == df_labels.shape[0]
    assert df_features.shape[1] == df_test_features.shape[1]

    level1ModelEval = []
    level1Modeltrain = []
    level2ModelEval = []
    level2Modeltrain = []

    allPredictList = []
    testPredictList = []

    trainPredictList = []
    evalPredictList = []

    # 将训练集切分成一个训练集和一个验证集
    df_train_features, df_eval_features, df_train_label, df_eval_label = splitTrainData(df_features, df_labels)

    for i in range(5):
        trainLabels = []
        evalLabels = []
        for j in range(len(df_train_label)):
            trainLabels.append(df_train_label.values[j][i])
        for j in range(len(df_eval_label)):
            evalLabels.append(df_eval_label.values[j][i])

        # 根据某一label，训练针对这个label的模型
        modleLevel1 = train_model_level1(df_train_features, trainLabels, df_eval_features, evalLabels)

        # 预测全集label与测试集label
        allPredict = modleLevel1.predict(df_features, num_iteration=modleLevel1.best_iteration)
        testPredict = modleLevel1.predict(df_test_features, num_iteration=modleLevel1.best_iteration)

        # 预测训练集label和验证集label
        trainPredict = modleLevel1.predict(df_train_features, num_iteration=modleLevel1.best_iteration)
        evalPredict = modleLevel1.predict(df_eval_features, num_iteration=modleLevel1.best_iteration)

        # 收集预测结果
        allPredictList.append(allPredict)
        testPredictList.append(testPredict)

        trainPredictList.append(trainPredict)
        evalPredictList.append(evalPredict)

        # 评估第一级分类器的指标
        level1ModelEval.append(metric(evalPredict.tolist(), evalLabels))
        level1Modeltrain.append(metric(trainPredict.tolist(), trainLabels))

    """
    二级
    """
    # 对全集扩展5个label特征
    allExpandFeatures = []
    labelsAll = np.array(allPredictList).transpose()
    for m in range(len(df_features)):
        x = df_features[m].tolist()
        x.extend(labelsAll[m])
        allExpandFeatures.append(x)

    # 对测试集扩展5个label特征
    testExpandFeatures = []
    labelsTest = np.array(testPredictList).transpose()
    for m in range(len(df_test_features)):
        x = df_test_features[m].tolist()
        x.extend(labelsTest[m])
        testExpandFeatures.append(x)

    """
    # 对分割出的训练集扩展5个label特征
    trainExpandFeatures = []
    labelsTrain = np.array(trainPredictList).transpose()
    for m in range(len(df_train_features)):
        x = df_train_features[m].tolist()
        x.extend(labelsTrain[m])
        trainExpandFeatures.append(x)
    

    # 对分割出的验证集扩展5个label特征
    evalExpandFeatures = []
    labelsEval = np.array(evalPredictList).transpose()
    for m in range(len(df_eval_features)):
        x = df_eval_features[m].tolist()
        x.extend(labelsEval[m])
        evalExpandFeatures.append(x)
    """
    df_allExpandFeatures = pd.DataFrame(allExpandFeatures).values
    df_testExpandFeatures = pd.DataFrame(testExpandFeatures).values

    #df_trainExpandFeatures = pd.DataFrame(trainExpandFeatures).values
    #df_evalExpandFeatures = pd.DataFrame(evalExpandFeatures).values

    #print(df_allExpandFeatures.shape)
    #print(df_testExpandFeatures.shape)

    #print df_trainExpandFeatures[0]
    #print df_trainExpandFeatures[1]

    for i in range(5):
        trainLabels = []
        evalLabels = []
        totalLabels = []
        for j in range(len(df_train_label)):
            trainLabels.append(df_train_label.values[j][i])
        for j in range(len(df_eval_label)):
            evalLabels.append(df_eval_label.values[j][i])
        for j in range(len(df_labels)):
            totalLabels.append(df_labels.values[j][i])

        # 生成添加4个label的样本
        trainExpandFeatures = []
        labelsTrain = np.array(trainPredictList).transpose()
        for m in range(len(df_train_features)):
            x = df_train_features[m].tolist()
            #y = np.append(x, str(round(labelsTrain[m][0], 3)).decode('utf-8'))
            #x.extend(np.delete(labelsTrain[m], i))
            x.extend(labelsTrain[m])
            #print x
            trainExpandFeatures.append(x)

        evalExpandFeatures = []
        labelsEval = np.array(evalPredictList).transpose()
        for m in range(len(df_eval_features)):
            x = df_eval_features[m].tolist()
            #y = np.append(x, str(round(labelsEval[m][0], 3)).decode('utf-8'))
            #x.extend(np.delete(labelsEval[m], i))
            x.extend(labelsEval[m])
            #print x
            evalExpandFeatures.append(x)

        df_trainExpandFeatures = pd.DataFrame(trainExpandFeatures).values
        df_evalExpandFeatures = pd.DataFrame(evalExpandFeatures).values
        print df_trainExpandFeatures[0]
        print df_trainExpandFeatures[1]
        modelLevel2 = train_model_level2(df_trainExpandFeatures, trainLabels, df_eval_features, evalLabels)

        evalPredict = modelLevel2.predict(df_evalExpandFeatures, num_iteration=modelLevel2.best_iteration)
        level2ModelEval.append(metric(evalPredict, evalLabels))

        #totalModel = train_model_level2(df_allExpandFeatures, totalLabels, df_eval_features, evalLabels)
        trainPredict = modelLevel2.predict(df_trainExpandFeatures, num_iteration=modelLevel2.best_iteration)
        level2Modeltrain.append(metric(trainPredict, trainLabels))

        #trainlevel2ModelEval += metric(totalModel.predict(df_allExpandFeatures, num_iteration=totalModel.best_iteration), totalLabels)

    print level1ModelEval
    print "Level1 eval: %f" % (sum(level1ModelEval) / 5, )
    print level1Modeltrain
    print "Level1 train: %f" % (sum(level1Modeltrain) / 5,)
    print level2ModelEval
    print "Level2 eval: %f" % (sum(level2ModelEval) / 5, )
    print level2Modeltrain
    print "Level2 train: %f" % (sum(level2Modeltrain) / 5,)
    #print "train eval: %f" % (trainlevel2ModelEval / 5,)
    #print(total / 5)


"""
    final = []
    index = 0
    for i in range(len(res[0])):

        str_tmp = []

        str_tmp.append(df_test_data[0][i])
        for index in range(5):
            str_tmp.append(round(res[index][i], 3))

        final.append(str_tmp)



    save = pd.DataFrame(final)

    save.to_csv("./result.0.0.csv", index=False, header=False)
    print()
"""

