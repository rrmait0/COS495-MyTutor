from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.contrib.auth.decorators import login_required
from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from credentials.models import Profile
from django.db.models import Q

# Create your views here.

@api_view(['POST'])
def search_profiles(request):

    try:
        if request.POST['search'] == '':
            raise NameError()
        first_name = Q(first_name__contains = request.POST['search'] )
        last_name = Q(last_name__contains = request.POST['search'])
        username = Q(username__contains = request.POST['search'])
        user = User.objects.filter(first_name | last_name | username)

        return JsonResponse({"search_results": [{"username": x.username, "first_name": x.first_name,
                              "last_name": x.last_name} for x in user]})

    except:
        return JsonResponse([], safe = False)
