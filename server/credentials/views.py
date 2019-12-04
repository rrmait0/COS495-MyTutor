from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.contrib.auth.decorators import login_required
from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from .models import Profile
from .serializers import UserSerializer, ProfileSerializer


@api_view(['GET'])
def get_user_info(request, username):

    try:
        #make sure user exists
        user = User.objects.get(username=username)
    except:
        return Response(data={'status': 'fail'}, status=status.HTTP_404_NOT_FOUND)

    profile = Profile.objects.get(user=user)
    profile_serial = ProfileSerializer(profile)
    return Response(profile_serial.data)


@api_view(['POST'])
def create_user_view(request):

    user = UserSerializer(data=request.data)

    if user.is_valid():
        user_data = {"username": request.data['username'], "password": request.data['password'],
                "first_name": request.data['first_name'], "last_name": request.data['last_name'],
                "email": request.data['email']}

        data = {
            "user": user_data,
            'bio': request.data['bio']
        }
        profile = ProfileSerializer(data=data)
        if profile.is_valid():
            new_user = user.save()
            Profile.objects.create(user=new_user, bio=data['bio'])
            return Response(data={'status': 'success'})
        else:
            return Response(data=profile.errors, status=status.HTTP_404_NOT_FOUND)
        
    else:
        return Response(data=user.errors, status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def authentication_view(request):
    """
    Authenticate a user and log them in
    """
    
    data = request.data
    username = data.get('username', '')
    password = data.get('password', '')
    user = authenticate(request, username=username, password=password)    

    if user is not None:
        login(request, user)
        return JsonResponse({"status": "success"})
    else:
        return JsonResponse({"status": "fail"})

